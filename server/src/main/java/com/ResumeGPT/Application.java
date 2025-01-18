package com.ResumeGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import com.ResumeGPT.api.*;
import com.ResumeGPT.models.*;

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

