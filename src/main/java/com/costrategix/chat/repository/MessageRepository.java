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

    @Query("SELECT DISTINCT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, m.threadId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.fromId = :userId AND m.id = mr.messageId AND m.id = ma.messageId AND m.threadId IS NULL")
    List<MessageHistoryDto> getSentParentMessages(long userId);

    @Query("SELECT DISTINCT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, m.threadId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.id = mr.messageId AND m.id = ma.messageId AND m.threadId = :messageId AND (m.fromId = :recipientId OR mr.recipientId = :recipientId)")
    List<MessageHistoryDto> getRepliedMessages(long messageId, long recipientId);

    @Query("SELECT m FROM Message m WHERE m.subject LIKE %:query% OR m.content LIKE %:query%")
    List<Message> getMessageHistoryBySearch(String query);

    @Query("SELECT DISTINCT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, m.threadId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE mr.recipientId = :userId AND m.id = mr.messageId AND m.id = ma.messageId AND m.threadId IS NULL")
    List<MessageHistoryDto> getRecievedParentMessages(long userId);

    @Query("SELECT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, m.threadId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.id = :messageId AND m.id = mr.messageId AND m.id = ma.messageId")
    MessageHistoryDto getMessageByMessageId(long messageId);

    @Query("SELECT DISTINCT mr.recipientId FROM Message m, MessageRecipients mr WHERE m.id = mr.messageId AND m.id = :messageId")
    List<Long> getRecipientsIdsByMessageId(long messageId);
}
