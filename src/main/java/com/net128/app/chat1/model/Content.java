package com.net128.app.chat1.model;

import java.util.Objects;

public class Content implements JsonObject<Content> {
    public enum Type { TEXT, CUSTOM };
    public Type type;
    public String text;
    public String customId;

    public Content(){}
    public Content(String text) {
        this.type = Type.TEXT;
        this.text = text;
    }
}
