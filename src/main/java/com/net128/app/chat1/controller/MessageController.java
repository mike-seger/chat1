package com.net128.app.chat1.controller;

import com.net128.app.chat1.Message;
import com.net128.app.chat1.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository repository;

    @RequestMapping("/generate")
    public void generate(){
        repository.save(new Message("Jack", "Kim", "Hello"));
        repository.save(new Message("Adam", "KJackim", "Hello"));
        repository.save(new Message("Kim", "David", "Hello"));
        repository.save(new Message("David", "Adam", "Hello"));
        repository.save(new Message("Jack", "Kim", "Hello"));
    }

    @RequestMapping("/list")
    public List<Message> listAll(){
        return repository.findAll();
    }
}