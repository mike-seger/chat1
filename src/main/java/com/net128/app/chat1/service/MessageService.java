package com.net128.app.chat1.service;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Content;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.repository.AttachmentRepository;
import com.net128.app.chat1.repository.MessageRepository;

import com.net128.app.chat1.util.MimeUtil;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional(readOnly = true)
    public List<Message> findAllMessages() {
        return repository.findAll();
    }

    public List<Message> findUserMessages(String userId, String beforeMessageId, Integer maxResults) {
        if(maxResults==null) {
            maxResults=this.maxResults;
        }
        Pageable topResults = new PageRequest(0, maxResults);
        Page<Message> messagePage=repository.findByUserIdBeforeMessageId(userId, beforeMessageId, topResults);
        return messagePage.getContent();
    }

    @Transactional(readOnly = true)
    public Message getMessage(String messageId) {
        Message message = repository.getOne(messageId);
        return message;
    }

    public Message create(Message message) {
        repository.save(message);
        repository.flush();
        return getMessage(message.getId());
    }

    public Message create(String senderId, String recipientId, Content messageText, MultipartFile file) throws IOException {
        Message message = new Message(senderId, recipientId, messageText);
        repository.save(message);
        repository.flush();
        try (InputStream is=file.getInputStream()) {
            attachData(message, IOUtils.toByteArray(is));
            return message;
        }
    }

    public void attachData(String messageId, byte [] data) {
        attachData(repository.getOne(messageId), data);
    }

    private void attachData(Message message, byte [] data) {
        message.setMimeType(MimeUtil.mimeType(data));
        message.setLength(data.length);
        repository.save(message);
        repository.flush();
        attachmentRepository.deleteByMessage(message);
        attachmentRepository.flush();
        Attachment attachment = new Attachment(message, data);
        attachmentRepository.save(attachment);
    }

    @Transactional(readOnly = true)
    public void streamData(String messageId, OutputStream outputStream) throws IOException {
        Pageable singleResult = new PageRequest(0, 1);
        List<Attachment> attachments=attachmentRepository.findByMessage(repository.getOne(messageId), singleResult).getContent();
        if(attachments.size()>0) {
            try (InputStream is = new ByteArrayInputStream(attachments.get(0).getData())) {
                IOUtils.copy(is, outputStream);
            }
        }
    }
}
