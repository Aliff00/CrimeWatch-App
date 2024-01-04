package com.example.crimewatch.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class NewsApiHelper {

    private static final String API_KEY = "10ec41c9d413455bb706aaa6b7dbea86";
    //  private static final String API_URL = "https://newsapi.org/v2/everything?q=crime%20Selangor&apiKey=" + API_KEY;
    private static final String API_URL = "https://newsdata.io/api/1/news?apikey=pub_343476faae00b4f4b8c1a6918aaca961ef6de&q=selangor";


    public List<NewsItem> fetchData() {
        Executor executor = Executors.newSingleThreadExecutor();

        CompletableFuture<List<NewsItem>> future = CompletableFuture.supplyAsync(() -> {
            try {
                String jsonResult = downloadUrl(API_URL);
                return parseJson(jsonResult);
            } catch (IOException e) {
                Log.d("NewsFragment", "Hoii error");
                Log.d("NewsFragment", e.toString());
                throw new RuntimeException(e);
            }
        }, executor);

        // Wait for the CompletableFuture to complete and get the result
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("NewsFragment", "Error getting the result");
            Log.d("NewsFragment", e.toString());
            throw new RuntimeException(e);
        }
    }



    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // Set request headers
        connection.setRequestProperty("key", "Value");

        connection.connect();

        int responseCode = connection.getResponseCode();
        Log.d("NewsApiHelper", "HTTP Response Code: " + responseCode);

        try (InputStream stream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        }
    }



    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
    private List<NewsItem> parseJson(String jsonString) {
        List<NewsItem> newsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            //JSONArray articlesArray = jsonObject.getJSONArray("articles");
            JSONArray articlesArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleObject = articlesArray.getJSONObject(i);

                String title = articleObject.getString("title");

                // String url = articleObject.getString("url");
                String url = articleObject.getString("link");

                String imageUrl = articleObject.getString("image_url");
                int maxCharacters = 150;
                String description = articleObject.getString("description");
                // Truncate the description to approximately 150 characters
                if (description != null && description.length() > maxCharacters) {
                    description = description.substring(0, maxCharacters);

                    // Remove incomplete words at the end
                    int lastSpaceIndex = description.lastIndexOf(" ");
                    if (lastSpaceIndex != -1) {
                        description = description.substring(0, lastSpaceIndex) + "...";
                    }

                }
                NewsItem newsItem = new NewsItem(title, url, description, imageUrl);
                newsList.add(newsItem);

            }
            Log.d("NewsApi", newsList.toString());
        } catch (JSONException e) {
            Log.e("NewsApiHelper", "Error parsing JSON", e);
        }

        return newsList;
    }

    public static class NewsItem {
        private String title;
        private String url;
        private String description;
        private String imageUrl;

        public NewsItem(String title, String url, String description, String imageUrl) {
            this.title = title;
            this.url = url;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getUrl() {
            return url;
        }
    }
}