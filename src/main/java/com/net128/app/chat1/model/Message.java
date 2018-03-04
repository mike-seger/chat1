package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
public class Message extends Identifiable implements JsonObject {
    @JsonProperty(access = READ_ONLY)
    @Column(nullable = false)
    private LocalDateTime sent;

    @JsonProperty(access = READ_ONLY)
    private LocalDateTime delivered;

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

    @NotNull
    @JsonIgnore
    @Column(length = 4000)
    @Size(max = 4000)
    private String text;

    @Column(length = 129)
    @Size(min = 3, max = 129)
    private String mimeType;

    private int length;

    @Transient
    private Content content;

    public Message() {}
    public Message(String senderId, String recipientId, Content content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }

    @PostLoad
    private void postLoad() {
        if(delivered ==null) {
            delivered = LocalDateTime.now();
        }
        content = unmarshalMessageText(text);
    }

    @PrePersist
    private void prePersist() {
        sent = LocalDateTime.now();
        text = marshalMessageText(content);
    }

    @PreUpdate
    private void preUpdate() {
        text = marshalMessageText(content);
    }

    private String marshalMessageText(Content content) {
        if(content==null) {
            content = new Content();
        }
        return  content.toJson();
    }

    private Content unmarshalMessageText(String json) {
        Content content = new Content();
        if(json!=null) {
            content.fromJson(json);
        }
        return content;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }

    public LocalDateTime getDelivered() {
        return delivered;
    }

    public void setDelivered(LocalDateTime delivered) {
        this.delivered = delivered;
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

    public int getLength() { return length; }

    public void setLength(int length) { this.length = length; }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public String toString() {
        return toJson();
    }
}
