package com.net128.app.chat1;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Message {
    @Id
    @JsonProperty(access= JsonProperty.Access.READ_ONLY)
    public String id;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime sent;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime read;

    @NotNull
    public String senderId;

    @NotNull
    public String recipientId;

    public String message;
    public String mimeType;
    public byte [] data;

    public Message(){}
    public Message(String senderId, String recipientId, String message) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
    }

    @PostLoad
    private void postLoad() {
        if(read==null) {
            read=LocalDateTime.now();
        }
    }

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID().toString();
        sent=LocalDateTime.now();
    }
}
