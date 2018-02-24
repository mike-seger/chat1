package com.net128.app.chat1.repository;

import com.net128.app.chat1.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>{
    List<Message> findBySenderIdOrRecipientIdAndSentAfterOrderBySentDesc(String senderId, String recipientId, LocalDateTime sent);
    default List<Message> findByUserId(String userId) {
        return findBySenderIdOrRecipientIdAndSentAfterOrderBySentDesc(userId, userId,
            LocalDateTime.of(1900, 1, 1, 0, 0));
    }
}
