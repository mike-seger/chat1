package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@MappedSuperclass
public class MessageBase {
    @Id
    @JsonProperty(access = READ_ONLY)
    @Column(length = 36)
    @Size(max = 36)
    public String id;

    @JsonProperty(access = READ_ONLY)
    @Column(nullable = false)
    public LocalDateTime sent;

    @JsonProperty(access = READ_ONLY)
    public LocalDateTime read;

    @NotNull
    @Column(length = 256, nullable = false)
    @Size(max = 256)
    public String senderId;

    @NotNull
    @Column(length = 256, nullable = false)
    @Size(max = 256)
    public String recipientId;

    @Column(length = 4000)
    @Size(max = 4000)
    public String text;

    @Column(length = 129)
    @Size(min = 3, max = 129)
    public String mimeType;

    public MessageBase() {}
    public MessageBase(String senderId, String recipientId, String text) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
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
