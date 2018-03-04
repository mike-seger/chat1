package com.net128.app.chat1.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(title, content.title) &&
            Objects.equals(text, content.text) &&
            Objects.equals(description, content.description) &&
            Objects.equals(customId, content.customId) &&
            Objects.equals(imageUrl, content.imageUrl) &&
            Objects.equals(link, content.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text, description, customId, imageUrl, link);
    }
}
