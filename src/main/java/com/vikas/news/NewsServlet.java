package com.vikas.news;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.json.*;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class NewsServlet extends HttpServlet {

    private String apiKey;
    private String baseUrl;

    @Override
    public void init() throws ServletException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new ServletException("config.properties not found");

            Properties props = new Properties();
            props.load(input);

            apiKey = props.getProperty("NEWS_API_KEY");
            baseUrl = props.getProperty("NEWS_URL");

            if (apiKey == null || apiKey.isBlank())
                throw new ServletException("NEWS_API_KEY missing in config.properties");

        } catch (Exception e) {
            throw new ServletException("Failed to load config", e);
        }
    }

    private JSONArray fetchNews(String query, int page, int pageSize) throws IOException {
        String urlStr =
                baseUrl +
                "q=" + URLEncoder.encode(query, "UTF-8") +
                "&page=" + page +
                "&pageSize=" + pageSize +
                "&sortBy=publishedAt" +
                "&apiKey=" + apiKey;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder json = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) json.append(line);

        JSONObject obj = new JSONObject(json.toString());

        if (!obj.optString("status", "").equals("ok"))
            return new JSONArray(); // return empty, fallback later

        return obj.optJSONArray("articles") != null ? obj.getJSONArray("articles") : new JSONArray();
    }

    private JSONArray getNewsWithFallback(String query, int page, int size) throws IOException {
        JSONArray articles = fetchNews(query, page, size);

        if (articles.length() == 0) {
            // fallback to trending
            articles = fetchNews("breaking news", 1, size);
        }
        return articles;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        if (req.getServletPath().equals("/news-data")) {
            handleAjax(req, resp);
            return;
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void handleAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String query = req.getParameter("q");
        if (query == null || query.isBlank()) query = "india";

        int page = Integer.parseInt(req.getParameter("page") == null ? "1" : req.getParameter("page"));
        int size = Integer.parseInt(req.getParameter("pageSize") == null ? "10" : req.getParameter("pageSize"));

        JSONArray articles = getNewsWithFallback(query, page, size);

        JSONObject out = new JSONObject();
        out.put("status", "ok");
        out.put("articles", articles);

        resp.setContentType("application/json");
        resp.getWriter().write(out.toString());
    }
}
