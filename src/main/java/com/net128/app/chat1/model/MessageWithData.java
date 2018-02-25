package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Blob;

@Entity
@Table(name = "Message")
public class MessageWithData extends MessageBase {
    @Lob
    public byte [] data;

    public MessageWithData(){}

    public MessageWithData(String senderId, String recipientId, String text) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
    }
}
