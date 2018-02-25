package com.net128.app.chat1.service;

import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageWithData;
import com.net128.app.chat1.repository.MessageRepository;
import com.net128.app.chat1.repository.MessageWithDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
public class MessageService {
    @Inject
    private MessageRepository repository;

    @Inject
    private MessageWithDataRepository repositoryWithData;

    public List<MessageWithData> findAllMessages() {
        return repositoryWithData.findAll();
    }

    @Transactional
    public List<Message> findUserMessages(String userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    public Message save(MessageWithData message) {
        MessageWithData messageWithData = repositoryWithData.save(message);
        return repository.getOne(messageWithData.id);
    }
}
