package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageDraft;
import com.net128.app.chat1.service.MessageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import org.springframework.web.multipart.MultipartFile;

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
    private EntityLinks entityLinks;

    @ApiOperation(value = "Get a list of messages",
        notes = "Get a list of messages, sorted by sent date, then message id. Several query parameters can be set to filter the list.",
        nickname = "getMessages")
    @GetMapping
    public HttpEntity<Resources<Resource<Message>>> getMessages(
            @RequestParam(name="uid", required = false)
            String userId,
            @RequestParam(name="mid", required = false)
            String startMessageId,
            @RequestParam(name="nmax", required = false)
            Integer maxResults
        ){
        List<Message> messages = messageService.findUserMessages(userId, startMessageId, maxResults);
        List<Resource<Message>> messageResources = messages.stream().map(m -> new Resource<>(m)).collect(Collectors.toList());
        Resources<Resource<Message>> resources = new Resources<>(messageResources);
        resources.add(entityLinks.linkToCollectionResource(Message.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Send a single message",
        notes = "Send a single message. The message information is posted in a message draft.",
        nickname = "sendMessage")
    @PostMapping(consumes = "multipart/form-data")
    @ApiImplicitParams({
        @ApiImplicitParam(name="file", value = "file", dataType = "java.io.File", paramType = "form"),
        @ApiImplicitParam(name="messageDraft", value = "the message draft", required = true, dataType = "com.net128.app.chat1.model.MessageDraft", paramType = "form")
    })
    public Message sendMessage(
            HttpServletRequest request,
        @ApiParam(hidden=true)
        @RequestPart(name="file", required = false) MultipartFile file,
        @ApiParam(hidden=true)
        @RequestPart(name="messageDraft") MessageDraft messageDraft
    ) throws IOException {
        Message message= messageService.create(messageDraft.toMessage());
        if(file!=null) {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            message = messageService.attach(message.getId(), attachment);
        }
        return message;
    }

    @ApiOperation(value = "Get file content attached to a message",
        notes = "Get file content attached to a message by message id. The binary content is returned  with the original file name and the determined content type.",
        nickname = "getAttachment")
    @GetMapping("{messageId}/attachment")
    public void getAttachment(
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
        messageService.streamAttachment(messageId, stream);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Message not found")
    @SuppressWarnings("serial")
    public class NotFoundException extends RuntimeException {}
}
