package com.net128.app.chat1.repository;

import com.net128.app.chat1.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, String>{
    @Query("select m from Message m " +
            "where (:userId is null or m.senderId = :userId or m.recipientId = :userId) " +
            "  and (:id is null or m.id != :id) " +
            "  and (:sentBefore is null or m.sent <= :sentBefore)" +
            "order by m.sent desc, m.id"
    )
    Page<Message> findByUserIdSentBeforeAndNotMessageId(
        @Param("userId") String userId, @Param("id") String beforeMessageId,
        @Param("sentBefore") LocalDateTime sentBefore, Pageable pageable);

    default Page<Message> findByUserIdBeforeMessageId(
            String userId, String beforeMessageId, Pageable pageable) {
        LocalDateTime sentBefore=null;
        Message message=null;
        if(beforeMessageId!=null) {
            message=getOne(beforeMessageId);
        }
        if(message!=null) {
            sentBefore=message.getSent();
        } else {
            beforeMessageId=null;
        }
        return findByUserIdSentBeforeAndNotMessageId(userId, beforeMessageId, sentBefore, pageable);
    }
}
