package com.net128.app.chat1.controller;

import com.net128.app.chat1.Message;
import com.net128.app.chat1.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageService service;

    @RequestMapping("/generate")
    public void generate(){
        service.save(new Message("Jack", "Kim", "Hello"));
        service.save(new Message("Adam", "KJackim", "Hello"));
        service.save(new Message("Kim", "David", "Hello"));
        service.save(new Message("David", "Adam", "Hello"));
        service.save(new Message("Jack", "Kim", "Hello"));
    }

    @RequestMapping("/list")
    public List<Message> listAll(){
        return service.find("");
    }
}