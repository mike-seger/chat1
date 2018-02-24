package com.net128.app.chat1.repository;

import com.net128.app.chat1.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>{
}
