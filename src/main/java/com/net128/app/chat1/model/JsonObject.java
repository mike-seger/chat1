package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JsonObject {
    ObjectMapper mapper=new ObjectMapper();
    default String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot convert to Json", e);
        }
    }
    default void fromJson(String json) {
        try {
            mapper.readerForUpdating(this).readValue(json);
        } catch (IOException e) {
            throw new RuntimeException("Cannot convert from Json: "+json, e);
        }
    }
}
