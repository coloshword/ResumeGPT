package com.ResumeGPT.models;


public class Message {
    private String text;
    private MessageType type;
    public Message(String text, MessageType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public MessageType getType() {
        return this.type;
    }
}