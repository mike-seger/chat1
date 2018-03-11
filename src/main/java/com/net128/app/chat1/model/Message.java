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
public class Message extends Identifiable implements JsonObject<Message> {
    @JsonProperty(access = READ_ONLY)
    @Column(nullable = false)
    private LocalDateTime sent;

    @JsonProperty(access = READ_ONLY)
    private LocalDateTime delivered;

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

    @Transient
    private Content content;

    @Transient
    @JsonIgnore
    private int length;

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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        if(getId()!=null && Objects.equals(getId(), message.getId())) {
            return true;
        }
        return Objects.equals(getSent(), message.getSent()) &&
                Objects.equals(getDelivered(), message.getDelivered()) &&
                Objects.equals(getSenderId(), message.getSenderId()) &&
                Objects.equals(getRecipientId(), message.getRecipientId()) &&
                Objects.equals(getText(), message.getText()) &&
                Objects.equals(getMimeType(), message.getMimeType()) &&
                Objects.equals(getContent(), message.getContent());
    }

    @Override
    public int hashCode() {
        if(getId()!=null) {
            return Objects.hash(getId());
        }
        return Objects.hash(getSent(), getDelivered(), getSenderId(), getRecipientId(), getText(), getMimeType(), getContent());
    }

    public String toString() {
        return toJson();
    }
}
