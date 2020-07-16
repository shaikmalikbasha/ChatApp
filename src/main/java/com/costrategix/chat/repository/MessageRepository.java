package com.costrategix.chat.repository;

import com.costrategix.chat.model.Message;
import com.costrategix.chat.dto.MessageHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE MessageRecipients mr SET mr.isRead = true WHERE mr.messageId = :messageId")
    int updateMessageByMessageId(@Param("messageId") long messageId);

    @Query("SELECT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, mr.recipientId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.fromId = :userId AND m.id = mr.messageId AND m.id = ma.messageId")
    List<MessageHistoryDto> getMessageHistoryByUserId(long userId);

//    @Query("SELECT m FROM Message m INNER JOIN MessageRecipients mr ON m.id = mr.messageId WHERE m.subject LIKE %?1% OR m.content LIKE %?1% AND (m.fromId = :userId OR mr.recipientId = :userId)")
//    List<Message> getMessageHistoryBySearch(String query, long userId);

    @Query("SELECT new com.costrategix.chat.dto.MessageHistoryDto(m.id, m.subject, m.content, m.fromId, mr.recipientId, mr.isRead, ma.fileName) FROM Message m, MessageRecipients mr, MessageAttachment ma WHERE m.id = :messageId AND m.id = mr.messageId AND m.id = ma.messageId")
    MessageHistoryDto getMessageByMessageId(long messageId);
}
