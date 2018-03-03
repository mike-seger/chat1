package com.net128.app.chat1.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Message")
public class MessageWithData extends MessageBase {
    @Lob
    private byte [] data;

    public MessageWithData(){}

    public MessageWithData(String senderId, String recipientId, RichText richText) {
        super(senderId, recipientId, richText);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
