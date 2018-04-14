package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

@Entity
public class Message extends Identifiable implements JsonObject<Message> {
    @NotNull
    @Column(length = 256, nullable = false)
    @Size(max = 256)
    @ApiModelProperty(value = "The id of the sender (set automatically)", position = -99, required = true)
    private String senderId;

    @NotNull
    @Column(length = 256, nullable = false)
    @Size(max = 256)
    @ApiModelProperty(value = "The id of the recipient", position = -98, required = true)
    private String recipientId;

    @JsonProperty(access = READ_ONLY)
    @Column(nullable = false)
    @ApiModelProperty(value = "The date the message was sent (set automatically)", position = -97, required = true)
    private LocalDateTime sent;

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(value = "The date the message was delivered (set automatically)", position = -96)
    private LocalDateTime delivered;

    @NotNull
    @JsonIgnore
    @Column(length = 4000)
    @Size(max = 4000)
    private String text;

    @Transient
    @JsonUnwrapped
    @ApiModelProperty(value = "The 'pay load' of the message. All of its attributes are optional", position = -95)
    private Payload payload;

    @Transient
    @JsonIgnore
    private int length;

    public Message() {}
    public Message(String senderId, String recipientId, Payload payload) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.payload = payload;
    }

    @PreUpdate
    public void preUpdateMessage() {
        text = marshalMessageText(payload);
    }

    @PrePersist
    public void prePersistMessage() {
        sent = LocalDateTime.now();
        preUpdateMessage();
    }

    @PostLoad
    public void postLoadMessage() {
        if(delivered ==null) {
            delivered = LocalDateTime.now();
        }
        payload = unmarshalMessageText(text);
    }

    private String marshalMessageText(Payload payload) {
        if(payload ==null) {
            payload = new Payload();
        }
        return  payload.toJson();
    }

    private Payload unmarshalMessageText(String json) {
        Payload payload = new Payload();
        if(json!=null) {
            payload.fromJson(json);
        }
        return payload;
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

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
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
                Objects.equals(getPayload(), message.getPayload());
    }

    @Override
    public int hashCode() {
        if(getId()!=null) {
            return Objects.hash(getId());
        }
        return Objects.hash(getSent(), getDelivered(), getSenderId(), getRecipientId(), getText(), getPayload());
    }

    public String toString() {
        return toJson();
    }
}
