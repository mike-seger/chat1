package com.net128.app.chat1.service;

import static org.junit.Assert.*;

import com.net128.app.chat1.model.Payload;
import com.net128.app.chat1.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class MessageServiceTest {
    @Inject
    private MessageService service;

    private final static String senderId="senderId";
    private final static String otherSenderId="senderId2";
    private final static int nMessages=10;
    private final static int startMessageIndex=5;

    @Test
    public void testCreateAndGetMessage() {
        Message message = service.create(newMessage(senderId));
        assertEquals(message, service.getMessage(message.getId()));
    }

    @Test
    public void testFindUserMessages() {
        List<Message> messages=newSavedMessages(nMessages);
        List<Message> foundMessages=service.findUserMessages(senderId,null, nMessages);
        assertEquals(messages.size(), foundMessages.size());
    }

    @Test
    public void testFindUserMessagesAfter() {
        List<Message> messages=newSavedMessages(nMessages);
        Message message5=messages.get(5);
        List<Message> foundMessages=service.findUserMessages(senderId, message5.getId(), nMessages);
        assertEquals(messages.subList(startMessageIndex+1, nMessages), foundMessages);
        assertEquals(2*nMessages, service.findUserMessages(null, null, null).size());
    }

    @Test
    public void testFindUserMessagesBefore() {
        List<Message> messages=newSavedMessages(nMessages);
        Message message5=messages.get(5);
        List<Message> foundMessages=service.findUserMessages(senderId, message5.getId(), -nMessages);
        assertEquals(messages.subList(0, startMessageIndex), foundMessages);
        assertEquals(2*nMessages, service.findUserMessages(null, null, null).size());
    }

    private List<Message> newSavedMessages(int n) {
        List<Message> messages=new ArrayList<>();
        long time=System.currentTimeMillis();
        while(n-->0) {
            while(time==System.currentTimeMillis());
            messages.add(service.create(newMessage(senderId)));
            service.create(newMessage(otherSenderId));
        }
        return messages;
    }

    private Message newMessage(String senderId) {
        return new Message(senderId, "recipientId", new Payload("content"));
    }
}
