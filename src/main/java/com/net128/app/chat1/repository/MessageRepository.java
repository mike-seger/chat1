package com.net128.app.chat1.repository;

import com.net128.app.chat1.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("select m from Message m " +
        "where (:userId is null or m.senderId = :userId or m.recipientId = :userId) " +
        "  and (:id is null or m.id != :id) " +
        "  and ( :date is null " +
        "    or (:before = true and m.sent <= :date) " +
        "    or (:before = false and m.sent >= :date) ) "
    )
    Page<Message> findByUserIdSentAroundMessageId(
        @Param("userId") String userId,
        @Param("id") String startMessageId,
        @Param("date") LocalDateTime date,
        @Param("before") boolean sentBefore,
        Pageable pageable);

    default Page<Message> findByUserIdAroundMessageId(
            String userId, String startMessageId, boolean sentBefore, Pageable pageable) {
        LocalDateTime sent=null;
        Message message=null;
        if(pageable==null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, getSort(startMessageId, sentBefore));
        }
        if(startMessageId!=null) {
            message=getOne(startMessageId);
        }
        if(message!=null) {
            sent=message.getSent();
        }
        return findByUserIdSentAroundMessageId(userId, startMessageId, sent, sentBefore, pageable);
    }

    default Page<Message> findByUserIdAroundMessageId(
            String userId, String startMessageId, boolean sentBefore, int nMax) {
        Pageable pageable = PageRequest.of(0, nMax, getSort(startMessageId, sentBefore));
        return findByUserIdAroundMessageId(userId, startMessageId, sentBefore, pageable);
    }

    default Page<Message> findByUserIdAroundMessageId(
            String userId, String startMessageId, boolean sentBefore) {
        return findByUserIdAroundMessageId(userId, startMessageId, sentBefore,null);
    }

    default Sort getSort(String startMessageId, boolean sentBefore){
        Sort sort;
        if(sentBefore || startMessageId==null) {
            sort = new Sort(Sort.Direction.DESC, "sent", "id");
        } else {
            sort = new Sort(Sort.Direction.ASC, "sent", "id");
        }
        return sort;
    }
}
