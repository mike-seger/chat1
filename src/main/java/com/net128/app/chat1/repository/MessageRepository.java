package com.net128.app.chat1.repository;

import com.net128.app.chat1.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String>{
    LocalDateTime beforeAnyMessage = LocalDateTime.of(1900, 1, 1, 0, 0);
    List<Message> findBySenderIdAndSentAfterOrRecipientIdAndSentAfterOrderBySentDesc(String senderId, LocalDateTime sent1, String recipientId, LocalDateTime sent2);
    default List<Message> findByUserId(String userId) {
        return findBySenderIdAndSentAfterOrRecipientIdAndSentAfterOrderBySentDesc(userId, beforeAnyMessage, userId, beforeAnyMessage);
    }
}
