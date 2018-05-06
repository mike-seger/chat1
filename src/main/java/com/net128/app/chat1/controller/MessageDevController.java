package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Payload;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Random;

@RestController
@Profile({"local", "perftest"})
public class MessageDevController {
    @Inject
    private MessageService messageService;

    private Random random=new Random(123987);

    @GetMapping("/generate/{n}")
    public void generate(@PathVariable("n") int n){
        for(int i=0;i<n;i++) {
            char fromId=randomId(n);
            char toId=randomId(n);
            Message message=messageService.create(new Message("TestUserID "+fromId,
                "TestUserID "+toId, new Payload("Hello "+i)));
            if(i%2==0) {
                messageService.attach(message.getId(), new Attachment(message, ("Hello "+i+" from "+fromId).getBytes()));
            }
        }
    }

    private char randomId(int n) {
        return (char)('A'+Math.round(random.nextDouble()*(n-1)));
    }
}
