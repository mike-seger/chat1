package com.net128.app.chat1.service;

import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.MessageWithData;
import com.net128.app.chat1.repository.MessageRepository;
import com.net128.app.chat1.repository.MessageWithDataRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

@Service
public class MessageService {
    @Inject
    private MessageRepository repository;

    @Inject
    private MessageWithDataRepository repositoryWithData;

    @Inject
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<MessageWithData> findAllMessages() {
        return repositoryWithData.findAll();
    }

    @Transactional
    public List<Message> findUserMessages(String userId) {
        return repository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Message getMessage(String messageId) {
        return repository.getOne(messageId);
    }

    @Transactional
    public Message save(MessageWithData message) {
        MessageWithData messageWithData = repositoryWithData.save(message);
        return repository.getOne(messageWithData.id);
    }

    @Transactional
    public Message saveData(Message message, InputStream inputStream) throws IOException {
        try (Session session = sessionFactory.openSession()) {
            LobHandler lobHandler = new DefaultLobHandler();
            SqlLobValue sqlLobValue = new SqlLobValue(inputStream, -1, lobHandler);
            return message;
        }
    }

    @Transactional(readOnly = true)
    public void loadData(String messageId, OutputStream outputStream) throws IOException {
        MessageWithData message=repositoryWithData.getOne(messageId);
        try (InputStream is = message.blob.getBinaryStream()) {
            IOUtils.copy(is, outputStream);
        } catch (SQLException e) {
            throw new IOException("Cannot extract BLOB for attachment #" + messageId, e);
        }
    }
}
