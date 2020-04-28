package org.selyu.messaging.model;

public final class Message {
    private final String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
