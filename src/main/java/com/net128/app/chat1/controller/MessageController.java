package com.net128.app.chat1.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.net128.app.chat1.Message;
import com.net128.app.chat1.service.MessageService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
public class MessageController {
    @Inject
    private MessageService service;

    @RequestMapping("/generate")
    public void generate(){
        service.save(new Message("Jack", "Kim", "Hello"));
        service.save(new Message("Adam", "KJackim", "Hello"));
        service.save(new Message("Kim", "David", "Hello"));
        service.save(new Message("David", "Adam", "Hello"));
        service.save(new Message("Jack", "Kim", "Hello"));
    }

    @RequestMapping(value="/usermessage/{userid}", method=GET)
    public List<Message> listAll(@PathVariable("userid") String userid){
        return service.findUserMessages(userid);
    }

    @RequestMapping(value="/save", method=POST)
    public Message save(Message message) {
        return service.save(message);
    }
}