package com.net128.app.chat1.model;

import javax.persistence.*;

@Entity
@Table(name = "Message")
public class Message extends MessageBase {
    public Message(){}
    public Message(String senderId, String recipientId, RichText richText) {
        super(senderId, recipientId, richText);
    }
}
