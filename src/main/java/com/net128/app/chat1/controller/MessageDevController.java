package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Content;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Random;

@RestController
@Profile("dev")
public class MessageDevController {
    @Inject
    private MessageService service;

    private Random random=new Random(123987);

    @PostMapping("/generate/{n}")
    public void generate(@PathVariable("n") int n){

        for(int i=0;i<n;i++) {
            char fromId=randomId(n);
            char toId=randomId(n);
            service.create(new Message("TestUserID "+fromId, "TestUserID "+toId, new Content("Hello "+i)));
        }
    }

    private char randomId(int n) {
        return (char)('A'+Math.round(random.nextDouble()*(n-1)));
    }
}
