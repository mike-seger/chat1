package com.net128.app.chat1.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Message")
public class MessageWithData extends MessageBase {
    @Lob
    public byte [] data;

    public MessageWithData(){}

    public MessageWithData(String senderId, String recipientId, String text) {
        this(senderId, recipientId, text, null);
    }

    public MessageWithData(String senderId, String recipientId, String text, byte [] data) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
        this.data = data;
    }
}
