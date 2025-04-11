package com.example.caretail;

public class Message {
    private String sender;
    private String text;
    private long timestamp;

    // 🔧 Required empty constructor for Firebase
    public Message() {
    }

    public Message(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
