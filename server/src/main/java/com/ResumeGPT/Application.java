package com.ResumeGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;

// HTTP request bodies (this should be in another file)
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

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
        System.out.println(sentChat.getText());
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
    private HttpClient client;

    public GeminiRequester() {
        // create the HTTPClient
        this.client = HttpClients.createDefault();
    }

   public void geminiPost(String content) {
       HttpPost post = new HttpPost(this.geminiEndpoint);
       // set request parameters    

   }
}
