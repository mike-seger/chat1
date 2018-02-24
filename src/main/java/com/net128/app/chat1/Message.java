package com.net128.app.chat1;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Message {
    @Id
    @JsonProperty(value="_id", access= JsonProperty.Access.READ_ONLY)
    public String id;
    @NotNull
    public LocalDateTime sent;
    public LocalDateTime read;
    @NotNull
    public String sender;
    @NotNull
    public String recipient;
    public String message;
    public String mimeType;
    public byte [] data;

    public Message(){}
    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
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
