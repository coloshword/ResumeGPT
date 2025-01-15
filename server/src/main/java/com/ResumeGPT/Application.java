package com.ResumeGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication
@CrossOrigin(origins = "*")
@RestController
public class Application {
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

}
 
class Message {
    private String text;

    public String getText() {
        return text;
    }
}
