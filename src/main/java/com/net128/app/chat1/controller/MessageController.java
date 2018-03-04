package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Content;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/messages")
    public Message upload(
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam("text") Content messageText,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.create(senderId, recipientId, messageText, file.getInputStream());
    }

    @GetMapping(value = "/messages/{messageId}/data")
    public void getData(
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = service.getMessage(messageId);
        response.setContentType(message.getMimeType());
        if(message.getLength()>0) {
            response.setContentLength(message.getLength());
        }
        service.streamData(messageId, stream);
    }

    @PutMapping(value = "/messages/{messageId}/data")
    public void putData(
            @PathVariable("messageId") String messageId,
            @RequestBody byte [] data) {
        service.attachData(messageId, data);
    }
}
