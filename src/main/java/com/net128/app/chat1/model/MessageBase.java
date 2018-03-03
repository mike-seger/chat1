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

@MappedSuperclass
public abstract class MessageBase implements JsonObject {
    @Id
    @JsonProperty(access = READ_ONLY)
    @Column(length = 32)
    @Size(max = 32)
    private String id;

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

    @Transient
    private RichText richText;

    public MessageBase() {}
    public MessageBase(String senderId, String recipientId, RichText richText) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.richText = richText;
    }

    @PostLoad
    private void postLoad() {
        if(delivered ==null) {
            delivered = LocalDateTime.now();
        }
        richText = unmarshalMessageText(text);
    }

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID().toString().replace("-", "");
        sent = LocalDateTime.now();
        text = marshalMessageText(richText);
    }

    @PreUpdate
    private void preUpdate() {
        text = marshalMessageText(richText);
    }

    private String marshalMessageText(RichText richText) {
        if(richText==null) {
            richText = new RichText();
        }
        return  richText.toJson();
    }

    private RichText unmarshalMessageText(String json) {
        RichText richText = new RichText();
        if(json!=null) {
            richText.fromJson(json);
        }
        return richText;
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

    public RichText getRichText() {
        return richText;
    }

    public void setRichText(RichText richText) {
        this.richText = richText;
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

    public String toString() {
        return toJson();
    }
}
