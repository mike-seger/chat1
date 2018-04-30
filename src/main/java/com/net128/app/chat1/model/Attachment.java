package com.net128.app.chat1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Attachment extends Identifiable {
    @JsonIgnore
    @ManyToOne
    private Message message;

    @Lob
    private byte [] data;

    private String fileName;
    @JsonIgnore
    private String mimeType;

    public Attachment(){}

    public Attachment(Message message, byte [] data) {
        this.message = message;
        this.data = data;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
