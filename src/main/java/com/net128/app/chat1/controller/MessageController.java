package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.RichText;
import com.net128.app.chat1.model.MessageWithData;
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

    @GetMapping(value="/user/{userid}/messages")
    public List<Message> forUser(@PathVariable("userid") String userId ){
        return service.findUserMessages(userId);
    }

    @PostMapping(value="/messages")
    public Message create(@RequestBody MessageWithData message) {
        return service.create(message);
    }

    @GetMapping(value="/messages")
    public List<MessageWithData> list() {
        return service.findAllMessages();
    }

    @PutMapping(value = "/messages/{messageId}/data")
    public void putData(
            @PathVariable("messageId") String messageId,
            @RequestBody byte [] data) throws IOException {
        service.attachData(messageId, data);
    }

    @GetMapping(value = "/messages/{messageId}/data")
    public void getData(
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = service.getMessage(messageId);
        response.setContentType(message.getMimeType());
        service.streamData(messageId, stream);
    }

    @PostMapping("/message-upload")
    public Message upload(
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam("text") RichText messageText,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.create(senderId, recipientId, messageText, file.getInputStream());
    }
}
