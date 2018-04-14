package com.net128.app.chat1.service;

import com.net128.app.chat1.model.*;
import com.net128.app.chat1.repository.AttachmentRepository;
import com.net128.app.chat1.repository.MessageRepository;

import com.net128.app.chat1.util.MimeUtil;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Service
@Transactional
public class MessageService {
    @Inject
    private MessageRepository repository;

    @Inject
    private AttachmentRepository attachmentRepository;

    private @Value("${chat1.messages.repository.maxresults}") int maxResults;

    public List<Message> findUserMessages(String userId, String aroundMessageId, Integer maxResults) {
        boolean sentBefore=false;
        if(maxResults==null) {
            maxResults=this.maxResults;
        } else if(maxResults < 0) {
            maxResults=-maxResults;
            sentBefore=true;
        }
        Page<Message> messagePage=repository.findByUserIdAroundMessageId(userId, aroundMessageId, sentBefore, maxResults);
        return messagePage.getContent();
    }

    @Transactional(readOnly = true)
    public Message getMessage(String messageId) {
        Message message = repository.findOne(messageId);
        return message;
    }

    public Message create(Message message) {
        message.setSenderId(getUserDetails().getUsername());
        return repository.saveAndFlush(message);
    }

    public void attach(String messageId, Attachment attachment) {
        Message message = repository.findOne(messageId);
        if(message==null) {
            return;
        }
        Payload payload = message.getPayload();
        if(payload ==null) {
            payload = new Payload();
        }
        attachment.setMimeType(MimeUtil.mimeType(attachment.getData()));
        payload.attachmentInfo=new Payload.AttachmentInfo(attachment.getMimeType(), attachment.getFileName());
        message.setPayload(payload);
        message.setText(null);
        message.setLength(attachment.getData().length);
        message = repository.saveAndFlush(message);

        attachmentRepository.deleteByMessage(message);
        attachmentRepository.flush();
        attachment.setMessage(message);
        attachmentRepository.saveAndFlush(attachment);
    }

    @Transactional(readOnly = true)
    public void streamAttachment(String messageId, OutputStream outputStream) throws IOException {
        Pageable singleResult = new PageRequest(0, 1);
        List<Attachment> attachments=attachmentRepository.findByMessage(repository.getOne(messageId), singleResult).getContent();
        if(attachments.size()>0) {
            try (InputStream is = new ByteArrayInputStream(attachments.get(0).getData())) {
                IOUtils.copy(is, outputStream);
            }
        }
    }

    private UserDetails getUserDetails() {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }
}
