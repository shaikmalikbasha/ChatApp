package com.costrategix.chat.repository;

import com.costrategix.chat.model.MessageRecipients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRecipientRepository extends JpaRepository<MessageRecipients, Long> {
}
