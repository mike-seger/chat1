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
        return message;
    }

    @Transactional
    public Message create(MessageWithData message) {
        repositoryWithData.save(message);
        repositoryWithData.flush();
        return getMessage(message.getId());

    }

    @Transactional
    public Message create(String senderId, String recipientId, String text, InputStream inputStream) throws IOException {
        MessageWithData message = new MessageWithData(senderId, recipientId, text);
        message.setData(IOUtils.toByteArray(inputStream));
        repositoryWithData.save(message);
        repositoryWithData.flush();
        return getMessage(message.getId());
    }

    @Transactional
    public void attachData(String messageId, byte [] data) throws IOException {
        MessageWithData message = repositoryWithData.getOne(messageId);
        message.setData(data);
        repositoryWithData.save(message);
    }

    @Transactional(readOnly = true)
    public void streamData(String messageId, OutputStream outputStream) throws IOException {
        MessageWithData message=repositoryWithData.getOne(messageId);
        if(message.getData()!=null) {
            try (InputStream is = new ByteArrayInputStream(message.getData())) {
                IOUtils.copy(is, outputStream);
            }
        }
    }
}
