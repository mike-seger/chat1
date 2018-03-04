package com.net128.app.chat1.repository;

import com.net128.app.chat1.model.Attachment;
import com.net128.app.chat1.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    long deleteByMessage(Message message);
    Page<Attachment> findByMessage(Message message, Pageable pageable);
}
