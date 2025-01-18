package com.ResumeGPT.api;
import java.util.List;
import java.util.ArrayList;

// defines the content level body
public class Content {
    public List<Part> parts;

    public Content(String text) {
        // content is a list of Part type 
        this.parts = new ArrayList<>();
        this.parts.add(new Part(text));
    }
}
