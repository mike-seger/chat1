package com.net128.app.chat1.model;

import javax.persistence.*;

@Entity
public class Attachment extends Identifiable {
    @ManyToOne
    private Message message;

    @Lob
    private byte [] data;

    public Attachment(){}

    public Attachment(Message message, byte [] data) {
        this.message = message;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
