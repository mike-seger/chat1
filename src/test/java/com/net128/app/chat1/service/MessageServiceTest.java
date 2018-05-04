package com.net128.app.chat1.service;

import static org.junit.Assert.*;

import com.net128.app.chat1.model.Payload;
import com.net128.app.chat1.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Rollback
@Transactional
@WithMockUser(username = "user", authorities = { "USER" })
public class MessageServiceTest {
    @Inject
    private MessageService service;

    private final static String senderId="user";
    private final static int nMessages=10;
    private final static int startMessageIndex=5;

    @Test
    public void testCreateAndGetMessage() {
        Message message = newMessage(senderId);
        Message messageSent = service.create(message);
        assertEquals(messageSent, service.getMessage(messageSent.getId()));
    }

    @Test
    public void testFindUserMessage() {
        Message message = newMessage(senderId);
        Message messageSent = service.create(message);
        List<Message> foundMessages=service.findUserMessages(senderId,null, nMessages);
        assertEquals(1, foundMessages.size());
        assertEquals(messageSent, foundMessages.get(0));
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
    }

    @Test
    public void testFindUserMessagesBefore() {
        List<Message> messages=newSavedMessages(nMessages);
        Message message5=messages.get(5);
        List<Message> foundMessages=service.findUserMessages(senderId, message5.getId(), -nMessages);
        assertEquals(messages.subList(0, startMessageIndex), foundMessages);
    }

    private List<Message> newSavedMessages(int n) {
        List<Message> messages=new ArrayList<>();
        long time;
        while(n-->0) {
            messages.add(service.create(newMessage(senderId)));
            time=System.currentTimeMillis();
            while(time==System.currentTimeMillis());
        }
        return messages;
    }

    private Message newMessage(String senderId) {
        return new Message(senderId, "recipientId", new Payload("content"));
    }
}
