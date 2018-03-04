package com.net128.app.chat1.model;

public class Content implements JsonObject {
    public String title;
    public String text;
    public String description;
    public String customId;
    public String imageUrl;
    public String link;

    public Content(){}
    public Content(String text) {
        this.text = text;
    }
}
