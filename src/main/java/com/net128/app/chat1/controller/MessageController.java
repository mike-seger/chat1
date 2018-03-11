package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;
import com.net128.app.chat1.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
public class MessageController {
    @Inject
    private MessageService messageService;

    @Inject
    private UserService userService;

    @GetMapping(value="/messages")
    public List<Message> getMessages(
            HttpServletRequest request,
            @RequestParam(name="userId", required = false) String userId,
            @RequestParam(name="startMessageId", required = false) String startMessageId,
            @RequestParam(name="nmax", required = false) Integer maxResults
        ){
        return messageService.findUserMessages(
            userService.getUserContext(request), userId, startMessageId, maxResults);
    }

    @PostMapping(value="/messages")
    public Message sendMessage(
            HttpServletRequest request,
            @RequestBody Message message) {
        return messageService.create(userService.getUserContext(request), message);
    }

    @GetMapping(value = "/messages/{messageId}/attachment")
    public void getAttachment(
            HttpServletRequest request,
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = messageService.getMessage(messageId);
        response.setContentType(message.getMimeType());
        if(message.getLength()>0) {
            response.setContentLength(message.getLength());
        }
        messageService.streamAttachment(userService.getUserContext(request), messageId, stream);
    }

    @PutMapping(value = "/messages/{messageId}/attachment")
    public void putAttachment(
            HttpServletRequest request,
            @PathVariable("messageId") String messageId,
            @RequestBody Attachment attachment) {
        messageService.attach(userService.getUserContext(request), messageId, attachment);
    }
}
