package com.net128.app.chat1.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageWithData;
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
        service.save(new MessageWithData("TestUserID A", "TestUserID C", "Hello"));
        service.save(new MessageWithData("TestUserID B", "TestUserID A", "Hello"));
        service.save(new MessageWithData("TestUserID C", "TestUserID D", "Hello"));
        service.save(new MessageWithData("TestUserID D", "TestUserID B", "Hello"));
        service.save(new MessageWithData("TestUserID A", "TestUserID C", "Hello"));
    }

    @RequestMapping(value="/usermessage/{userid}", method=GET)
    public List<Message> userMessages(@PathVariable("userid") String userId ){
        return service.findUserMessages(userId);
    }

    @RequestMapping(value="/findall", method=GET)
    public List<MessageWithData> findAll() {
        return service.findAllMessages();
    }

    @RequestMapping(value="/save", method=POST)
    public Message save(MessageWithData message) {
        return service.save(message);
    }
}
