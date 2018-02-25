package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageWithData;
import com.net128.app.chat1.service.MessageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
        service.save(new MessageWithData("TestUserID A", "TestUserID C", "Hello"));
        service.save(new MessageWithData("TestUserID B", "TestUserID A", "Hello"));
        service.save(new MessageWithData("TestUserID C", "TestUserID D", "Hello"));
        service.save(new MessageWithData("TestUserID D", "TestUserID B", "Hello"));
        service.save(new MessageWithData("TestUserID A", "TestUserID C", "Hello"));
    }

    @GetMapping(value="/usermessage/{userid}")
    public List<Message> userMessages(@PathVariable("userid") String userId ){
        return service.findUserMessages(userId);
    }

    @GetMapping(value="/findall")
    public List<MessageWithData> findAll() {
        return service.findAllMessages();
    }

    @PostMapping(value="/save")
    public Message save(MessageWithData message) {
        return service.save(message);
    }

    @PostMapping("/upload")
    public Message upload(
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam("text") String text,
            @RequestParam("data") MultipartFile data) throws IOException {
        return service.saveData(new Message(senderId, recipientId, text), data.getInputStream());
    }

    @PutMapping(value = "/data/{messageId}")
    public Message putData(
            @PathVariable("messageId") String messageId,
            HttpServletRequest request) throws IOException {
        Message message = service.getMessage(messageId);
        return service.saveData(message, request.getInputStream());
    }

    @GetMapping(value = "/data/{messageId}")
    public void getData(
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = service.getMessage(messageId);
        response.setContentType(message.mimeType);
        service.loadData(messageId, stream);
    }
}
