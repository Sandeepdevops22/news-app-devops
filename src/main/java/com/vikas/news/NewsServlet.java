package com.vikas.news;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.json.*;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class NewsServlet extends HttpServlet {

    private String apiKey;

    @Override
    public void init() throws ServletException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new ServletException("config.properties missing");

            Properties props = new Properties();
            props.load(in);

            apiKey = props.getProperty("NEWS_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                throw new ServletException("Missing NEWS_API_KEY in config.properties");
            }
        } catch (IOException e) {
            throw new ServletException("Failed to load config", e);
        }
    }

    private JSONObject fetchNews(String q, int page, int pageSize) throws IOException {

        String urlStr =
                "https://newsapi.org/v2/top-headlines?" +
                "country=in" +
                "&q=" + URLEncoder.encode(q, "UTF-8") +
                "&page=" + page +
                "&pageSize=" + pageSize +
                "&apiKey=" + apiKey;

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return new JSONObject(sb.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String servletPath = req.getServletPath();

        // AJAX call → returns JSON
        if ("/news-data".equals(servletPath)) {

            String q = req.getParameter("q");
            if (q == null || q.isBlank()) q = "latest";

            int page = Integer.parseInt(req.getParameter("page") == null ? "1" : req.getParameter("page"));
            int pageSize = 10;

            try {
                JSONObject result = fetchNews(q, page, pageSize);
                JSONObject out = new JSONObject();
                out.put("status", "ok");
                out.put("articles", result.optJSONArray("articles"));
                writeJson(resp, out);
            } catch (Exception e) {
                JSONObject out = new JSONObject();
                out.put("status", "error");
                out.put("message", e.getMessage());
                out.put("articles", new JSONArray());
                writeJson(resp, out);
            }

            return;
        }

        // Normal JSP load
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void writeJson(HttpServletResponse resp, JSONObject obj) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(obj.toString());
    }
}
