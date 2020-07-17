package com.costrategix.chat.repository;

import com.costrategix.chat.dto.MessageHistoryDto;
import com.costrategix.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE MessageRecipients mr SET mr.isRead = true WHERE mr.messageId = :messageId")
    int updateMessageByMessageId(@Param("messageId") long messageId);

    @Query("SELECT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, mr.recipientId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.fromId = :userId AND m.id = mr.messageId AND m.id = ma.messageId")
    List<MessageHistoryDto> getMessageHistoryByUserId(long userId);

    @Query("SELECT m FROM Message m WHERE m.subject LIKE %:query% OR m.subject LIKE %:query%")
    List<Message> getMessageHistoryBySearch(String query);

    @Query("SELECT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, mr.recipientId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.id = :messageId AND m.id = mr.messageId AND m.id = ma.messageId")
    MessageHistoryDto getMessageByMessageId(long messageId);
}
