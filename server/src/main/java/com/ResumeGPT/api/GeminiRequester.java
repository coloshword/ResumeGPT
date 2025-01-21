package com.ResumeGPT.api;
import org.springframework.stereotype.Component;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Component
public class GeminiRequester {
    private String API_key = 
    private String geminiEndpoint = String.format("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=%s", this.API_key); 

    public GeminiRequester() {
    }

    public String geminiPost(String content) {
        HttpURLConnection con = null;

        try {
            URL url = new URL(this.geminiEndpoint);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            GeminiRequest request = new GeminiRequest(content);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(request);

            // Send JSON payload
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) { // Success responses (2xx)
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    JsonNode jsonResponse = objectMapper.readTree(response.toString());
                    try {
                        JsonNode candidates = jsonResponse.path("candidates");
                        if (candidates.isArray() && candidates.size() > 0) {
                            JsonNode content_json = candidates.get(0).path("content");
                            JsonNode parts = content_json.path("parts").get(0);
                            String text = parts.path("text").asText();
                            return text;
                        }
                    }
                    catch (Exception e) {
                        System.err.println("Could not parse Gemini Response json");
                    }
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.err.println("Error Response: " + errorResponse.toString());
                }
            }

        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + e.getMessage());
        } catch (ProtocolException e) {
            System.err.println("Protocol error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return "";
    }

}
