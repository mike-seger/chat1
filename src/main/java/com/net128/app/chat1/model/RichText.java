package com.net128.app.chat1.model;

public class RichText implements JsonObject {
    public String title;
    public String text;
    public String description;
    public String customId;
    public String imageUrl;
    public String link;

    public RichText(){}
    public RichText(String text) {
        this.text = text;
    }
}
