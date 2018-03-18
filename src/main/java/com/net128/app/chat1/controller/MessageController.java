package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageDraft;
import com.net128.app.chat1.service.MessageService;
import com.net128.app.chat1.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/messages", produces = "application/json")
@ExposesResourceFor(Message.class)
public class MessageController {
    @Inject
    private MessageService messageService;

    @Inject
    private UserService userService;

    @Inject
    private EntityLinks entityLinks;

    @ApiOperation(value = "Get a list of messages",
        notes = "Get a list of messages, sorted by sent date, then message id. Several query parameters can be set to filter the list.",
        nickname = "getMessages")
    @GetMapping(produces = "application/hal+json")
    public HttpEntity<Resources<Resource<Message>>> getMessages(
            HttpServletRequest request,
            @RequestParam(name="uid", required = false)
            String userId,
            @RequestParam(name="mid", required = false)
            String startMessageId,
            @RequestParam(name="nmax", required = false)
            Integer maxResults
        ){
        List<Message> messages = getMessagesJson(request, userId, startMessageId, maxResults);
        List<Resource<Message>> messageResources = messages.stream().map(m -> new Resource<>(m)).collect(Collectors.toList());
        messageResources.forEach(mr -> mr.add(entityLinks.linkToSingleResource(Message.class, mr.getContent().getId()).withSelfRel() ));
        Resources<Resource<Message>> resources = new Resources<>(messageResources);
        resources.add(entityLinks.linkToCollectionResource(Message.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public List<Message> getMessagesJson(
            HttpServletRequest request,
            @RequestParam(name="uid", required = false)
            @ApiParam(value = "The user ID to search messages from")
            String userId,
            @RequestParam(name="mid", required = false)
            @ApiParam(value = "The message ID to start from")
            String startMessageId,
            @ApiParam(value = "The maximum number of messages to return. Negative values mean 'back in time'")
            @RequestParam(name="nmax", required = false)
            Integer maxResults
    ){
        List<Message> messages = messageService.findUserMessages(
            userService.getUserContext(request), userId, startMessageId, maxResults);
        return messages;
    }

    @ApiOperation(value = "Get a single message",
        notes = "Get a list of messages by message ID",
        nickname = "getMessage")
    @GetMapping(value = "{messageId}", produces = "application/hal+json")
    public HttpEntity<Resource<Message>> getMessage(
            @PathVariable("messageId") String messageId) {
        Message message = getMessageJson(messageId);
        Resource<Message> resource = new Resource<>(messageService.getMessage(messageId));
        resource.add(entityLinks.linkToSingleResource(Message.class, message.getId()).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping(value = "{messageId}", produces = "application/json")
    public Message getMessageJson(
            @PathVariable("messageId") String messageId) {
        Message message = messageService.getMessage(messageId);
        if(message==null) {
            throw new NotFoundException();
        }
        return message;
    }

    @ApiOperation(value = "Send a single message",
        notes = "Send a single message. The message information is posted in a message draft.",
        nickname = "sendMessage")
    @PostMapping(produces = "application/hal+json")
    public HttpEntity<Resource<Message>> sendMessage(
            HttpServletRequest request,
            @RequestBody MessageDraft messageDraft) {
        Message message = sendMessageJson(request, messageDraft);
        Resource<Message> resource = new Resource<>(message);
        resource.add(entityLinks.linkToSingleResource(Message.class, message.getId()).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation(value="", hidden = true)
    @PostMapping
    public Message sendMessageJson(
            HttpServletRequest request,
            @RequestBody MessageDraft messageDraft) {
        return messageService.create(userService.getUserContext(request), messageDraft.toMessage());
    }

    @ApiOperation(value = "Get file content attached to a message",
        notes = "Get file content attached to a message by message id. The binary content is returned  with the original file name and the determined content type.",
        nickname = "getAttachment")
    @GetMapping(value = "{messageId}/attachment")
    public void getAttachment(
            HttpServletRequest request,
            @PathVariable("messageId") String messageId,
            HttpServletResponse response,
            OutputStream stream) throws IOException {
        Message message = messageService.getMessage(messageId);
        if(message.getPayload()!=null && message.getPayload().attachmentInfo!=null) {
            response.setContentType(message.getPayload().attachmentInfo.mimeType);
        }
        if(message.getLength()>0) {
            response.setContentLength(message.getLength());
        }
        messageService.streamAttachment(userService.getUserContext(request), messageId, stream);
    }

    @ApiOperation(value = "Attach file content to a message",
        notes = "Attach file content to a message.",
        nickname = "putAttachment")
    @PutMapping(value = "{messageId}/attachment")
    public void putAttachment(
            HttpServletRequest request,
            @PathVariable("messageId") String messageId,
            @RequestBody Attachment attachment) {
        messageService.attach(userService.getUserContext(request), messageId, attachment);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Message not found")
    public class NotFoundException extends RuntimeException {}
}
