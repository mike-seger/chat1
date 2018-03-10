package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public interface JsonObject<T> {
    ObjectMapper mapper=new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    default String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot convert to Json", e);
        }
    }
    default T fromJson(String json) {
        try {
            mapper.readerForUpdating(this).readValue(json);
            return (T)this;
        } catch (IOException e) {
            throw new RuntimeException("Cannot convert from Json: "+json, e);
        }
    }
}
