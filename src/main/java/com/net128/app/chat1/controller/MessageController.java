package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Message;
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

    @RequestMapping("/generate")
    public void generate(){
        service.create(new MessageWithData("TestUserID A", "TestUserID C", "Hello"));
        service.create(new MessageWithData("TestUserID B", "TestUserID A", "Hello"));
        service.create(new MessageWithData("TestUserID C", "TestUserID D", "Hello"));
        service.create(new MessageWithData("TestUserID D", "TestUserID B", "Hello"));
        service.create(new MessageWithData("TestUserID A", "TestUserID C", "Hello"));
    }

    @GetMapping(value="/for-user/{userid}")
    public List<Message> forUser(@PathVariable("userid") String userId ){
        return service.findUserMessages(userId);
    }

    @GetMapping(value="/find-all")
    public List<MessageWithData> findAll() {
        return service.findAllMessages();
    }

    @PostMapping(value="/create")
    public Message create(@RequestBody MessageWithData message) {
        return service.create(message);
    }

    @PostMapping("/upload")
    public Message upload(
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam("text") String text,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.create(senderId, recipientId, text, file.getInputStream());
    }

    @PutMapping(value = "/data/{messageId}")
    public void putData(
            @PathVariable("messageId") String messageId,
            @RequestBody byte [] data) throws IOException {
        service.attachData(messageId, data);
    }

    @GetMapping(value = "/data/{messageId}")
    public void getData(
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = service.getMessage(messageId);
        response.setContentType(message.getMimeType());
        service.streamData(messageId, stream);
    }
}
