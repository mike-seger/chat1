package com.net128.app.chat1.service;

import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageWithData;
import com.net128.app.chat1.repository.MessageRepository;
import com.net128.app.chat1.repository.MessageWithDataRepository;

import org.apache.commons.io.IOUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class MessageService {
    @Inject
    private MessageRepository repository;

    @Inject
    private MessageWithDataRepository repositoryWithData;

    @Transactional(readOnly = true)
    public List<MessageWithData> findAllMessages() {
        return repositoryWithData.findAll();
    }

    @Transactional
    public List<Message> findUserMessages(String userId) {
        return repository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Message getMessage(String messageId) {
        Message message = repository.getOne(messageId);
        System.out.println(message.id);
        return message;
    }

    @Transactional
    public Message save(MessageWithData message) {
        MessageWithData messageWithData = repositoryWithData.save(message);
        return getMessage(messageWithData.id);
    }

    @Transactional
    public Message save(String messageId, InputStream inputStream) throws IOException {
        MessageWithData message = repositoryWithData.getOne(messageId);
        message.data = IOUtils.toByteArray(inputStream);
        repositoryWithData.save(message);
        return getMessage(messageId);
    }

    @Transactional
    public Message save(String senderId, String recipientId, String text, InputStream inputStream) throws IOException {
        MessageWithData messageWithData = new MessageWithData(senderId, recipientId, text);
        messageWithData.data = IOUtils.toByteArray(inputStream);
        messageWithData=repositoryWithData.save(messageWithData);
        return repository.getOne(messageWithData.id);
    }

    @Transactional(readOnly = true)
    public void loadData(String messageId, OutputStream outputStream) throws IOException {
        MessageWithData message=repositoryWithData.getOne(messageId);
        if(message.data!=null) {
            try (InputStream is = new ByteArrayInputStream(message.data)) {
                IOUtils.copy(is, outputStream);
            }
        }
    }
}
