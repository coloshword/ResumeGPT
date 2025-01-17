package com.ResumeGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.io.IOException;
// HTTP request bodies (this should be in another file)
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.ResumeGPT.api.GeminiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@CrossOrigin(origins = "*")
@RestController
public class Application {
    
    private final GeminiRequester geminiRequester;
    
    @Autowired 
    public Application(GeminiRequester geminiRequester) {
        this.geminiRequester = geminiRequester;
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @GetMapping("/home")
    public String hello() {
        return String.format("Hello World");
    }
    @PostMapping("/post-chat")
    public String postChat(@RequestBody Message sentChat) {
        String receivedChat = sentChat.getText();
        System.out.println(receivedChat);
        this.makeGeminiRequest(receivedChat);
        return "Success";
    }

    public String makeGeminiRequest(String requestText) {
        this.geminiRequester.geminiPost(requestText);
        return "Hello";
    }
}
 
class Message {
    private String text;
    public String getText() {
        return text;
    }
}

@Component
class GeminiRequester {
    private String API_key = 
    private String geminiEndpoint = String.format("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=%s", this.API_key); 

    public GeminiRequester() {
    }


    public void geminiPost(String content) {
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

            // Read the response
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode >= 200 && responseCode < 300) { // Success responses (2xx)
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    // Convert response string to JSON
                    JsonNode jsonResponse = objectMapper.readTree(response.toString());
                    System.out.println("Response JSON: " + jsonResponse.toPrettyString());
                }
            } else { // Handle error responses
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
    }


}
