package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
public class MessageController {
    @Inject
    private MessageService service;

    @GetMapping(value="/messages")
    public List<Message> getMessages(
            @RequestParam(name="userId", required = false) String userId,
            @RequestParam(name="beforeMessageId", required = false) String beforeMessageId,
            @RequestParam(name="nmax", required = false) Integer maxResults
        ){
        return service.findUserMessages(userId, beforeMessageId, maxResults);
    }


    @PostMapping(value="/messages")
    public Message sendMessages(@RequestBody Message message){
        return service.create(message);
    }

    @GetMapping(value = "/messages/{messageId}/attachment")
    public void getAttachment(
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = service.getMessage(messageId);
        response.setContentType(message.getMimeType());
        if(message.getLength()>0) {
            response.setContentLength(message.getLength());
        }
        service.streamAttachment(messageId, stream);
    }

    @PutMapping(value = "/messages/{messageId}/attachment")
    public void putAttachment(
            @PathVariable("messageId") String messageId,
            @RequestBody Attachment attachment) {
        service.attach(messageId, attachment);
    }
}
