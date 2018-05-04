package com.net128.app.chat1.repository;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.net128.app.chat1.model.Payload;
import com.net128.app.chat1.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
public class MessageRepositoryTest {
    @Inject
    private MessageRepository repository;

    private final static String senderId="senderId";

    @Test
    public void testSaveMessage() {
        Message message=repository.save(newMessage());
        assertNotNull(repository.findOne(message.getId()));
    }

    @Test
    public void testFindMessageBefore() {
        testFindMessagesAround(true);
    }

    @Test
    public void testFindMessageAfter() {
        testFindMessagesAround(false);
    }

    private void testFindMessagesAround(boolean before) {
        List<Message> messages = newSavedMessages(2);
        if(!before) {
            messages = Lists.reverse(messages);
        }
        Page<Message> foundMessages =
            repository.findByUserIdAroundMessageId(senderId,
                messages.get(1).getId(), before);
        assertEquals(1, foundMessages.getContent().size());
        assertEquals(messages.get(0).getId(), foundMessages.getContent().get(0).getId());
    }

    private List<Message> newSavedMessages(int n) {
        List<Message> messages=new ArrayList<>();
        long time=System.currentTimeMillis();
        while(n-->0) {
            while(time==System.currentTimeMillis());
            messages.add(repository.save(newMessage()));
        }
        return messages;
    }

    private Message newMessage() {
        return new Message(senderId, "recipientId", new Payload("content"));
    }
}
