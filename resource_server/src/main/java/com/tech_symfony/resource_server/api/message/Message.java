package com.tech_symfony.resource_server.api.message;

public class Message {
    private String content;

    private String to;

    public void setContent() {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
