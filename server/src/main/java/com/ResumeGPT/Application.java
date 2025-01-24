package com.ResumeGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity<Message> postChat(@RequestBody Message sentChat) {
        String receivedChat = sentChat.getText();
        try {
            System.out.println("here1");
            String chatAsString = new ObjectMapper().writeValueAsString(sentChat);
            System.out.println("here2");
            System.out.println(chatAsString);
            String geminiResponse = this.makeGeminiRequest(receivedChat);
            System.out.println("geminiResponse");
            System.out.println(geminiResponse);
            Message geminiMessage = new Message(geminiResponse, MessageType.LLM);
            return new ResponseEntity<Message>(geminiMessage, HttpStatus.OK);
        }
        catch (JsonProcessingException e) {
            System.out.println(e);
        }
        Message response = new Message("Failed to work", MessageType.LLM);
        return new ResponseEntity<Message>(response, HttpStatus.NOT_IMPLEMENTED);
    }

    public String makeGeminiRequest(String requestText) {
        return this.geminiRequester.geminiPost(requestText);
    }
}

