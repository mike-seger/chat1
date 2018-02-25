package com.net128.app.chat1.repository;

import com.net128.app.chat1.model.MessageWithData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageWithDataRepository extends JpaRepository<MessageWithData, String> {
}
