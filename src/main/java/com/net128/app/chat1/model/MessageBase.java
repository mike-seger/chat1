package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@MappedSuperclass
public class MessageBase {
    @Id
    @JsonProperty(access = READ_ONLY)
    @Column(length = 36)
    @Size(max = 36)
    private String id;

    @JsonProperty(access = READ_ONLY)
    @Column(nullable = false)
    private LocalDateTime sent;

    @JsonProperty(access = READ_ONLY)
    private LocalDateTime read;

    @NotNull
    @Column(length = 256, nullable = false)
    @Size(max = 256)
    private String senderId;

    @NotNull
    @Column(length = 256, nullable = false)
    @Size(max = 256)
    private String recipientId;

    @Column(length = 4000)
    @Size(max = 4000)
    private String text;

    @Column(length = 129)
    @Size(min = 3, max = 129)
    private String mimeType;

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
        sent = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }

    public LocalDateTime getRead() {
        return read;
    }

    public void setRead(LocalDateTime read) {
        this.read = read;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageBase that = (MessageBase) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
