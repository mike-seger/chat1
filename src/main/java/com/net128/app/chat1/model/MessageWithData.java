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
    private byte [] data;

    public MessageWithData(){}

    public MessageWithData(String senderId, String recipientId, String text) {
        super(senderId, recipientId, text);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
