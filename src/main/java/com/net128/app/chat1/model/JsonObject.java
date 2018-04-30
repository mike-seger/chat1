package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface JsonObject<T> {
    ObjectMapper mapper=createObjectMapper();
    default String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot convert to Json", e);
        }
    }
    @SuppressWarnings("unchecked")
    default T fromJson(String json) {
        try {
            mapper.readerForUpdating(this).readValue(json);
            return (T)this;
        } catch (IOException e) {
            throw new RuntimeException("Cannot convert from Json: "+json, e);
        }
    }

    static ObjectMapper createObjectMapper() {
        ObjectMapper mapper=new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}
