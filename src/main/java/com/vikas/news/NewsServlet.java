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
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new ServletException("config.properties missing!");

            Properties p = new Properties();
            p.load(in);

            apiKey = p.getProperty("NEWS_API_KEY");
            baseUrl = p.getProperty("NEWS_URL");

            if (apiKey == null || apiKey.isEmpty())
                throw new ServletException("NEWS_API_KEY missing in config.properties");

        } catch (Exception e) {
            throw new ServletException("Failed to load config", e);
        }
    }

    private JSONObject fetch(String q, int page, int pageSize) throws IOException {
        String url = baseUrl +
                "q=" + URLEncoder.encode(q, "UTF-8") +
                "&sortBy=publishedAt" +
                "&language=en" +
                "&page=" + page +
                "&pageSize=" + pageSize +
                "&apiKey=" + apiKey;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        return new JSONObject(sb.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String servletPath = req.getServletPath();

        if ("/news-data".equals(servletPath)) {
            handleApi(req, resp);
            return;
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void handleApi(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String q = req.getParameter("q");
        String category = req.getParameter("category");

        String query = (q != null && !q.isEmpty())
                ? q
                : (category != null && !category.isEmpty() ? category : "india");

        int page = Integer.parseInt(req.getParameter("page") == null ? "1" : req.getParameter("page"));
        int pageSize = Integer.parseInt(req.getParameter("pageSize") == null ? "10" : req.getParameter("pageSize"));

        JSONObject out = new JSONObject();
        try {
            JSONObject json = fetch(query, page, pageSize);

            out.put("status", json.optString("status"));
            out.put("totalResults", json.optInt("totalResults"));
            out.put("articles", json.optJSONArray("articles"));

        } catch (Exception e) {
            out.put("status", "error");
            out.put("message", e.getMessage());
            out.put("articles", new JSONArray());
        }

        resp.setContentType("application/json");
        resp.getWriter().write(out.toString());
    }
}

