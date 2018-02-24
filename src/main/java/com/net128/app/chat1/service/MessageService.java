package com.net128.app.chat1.service;

import com.net128.app.chat1.Message;
import com.net128.app.chat1.repository.MessageRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class MessageService {
    @Inject
    private MessageRepository repository;

    public List<Message> find(String user) {
        return repository.findAll();
    }

    public void save(Message message) {
        repository.save(message);
    }
}
