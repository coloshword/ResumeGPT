// Gemini Payload: defines the payload to geminiA
// defines the top level gemini request body
package com.ResumeGPT.api;
import java.util.List;
import java.util.ArrayList;

public class GeminiRequest {
    public Content contents;
    
    public GeminiRequest(String text) {
        this.contents = new Content(text);
    }
}

