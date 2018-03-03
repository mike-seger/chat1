package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.MessageWithData;
import com.net128.app.chat1.model.RichText;
import com.net128.app.chat1.service.MessageService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@Profile("dev")
public class MessageDevController {
    @Inject
    private MessageService service;

    @GetMapping("/generate")
    public void generate(){
        service.create(new MessageWithData("TestUserID A", "TestUserID C", new RichText("Hello")));
        service.create(new MessageWithData("TestUserID B", "TestUserID A", new RichText("Hello")));
        service.create(new MessageWithData("TestUserID C", "TestUserID D", new RichText("Hello")));
        service.create(new MessageWithData("TestUserID D", "TestUserID B", new RichText("Hello")));
        service.create(new MessageWithData("TestUserID A", "TestUserID C", new RichText("Hello")));
    }
}
