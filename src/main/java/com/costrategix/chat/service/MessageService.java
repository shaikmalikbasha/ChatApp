package com.costrategix.chat.service;

import com.costrategix.chat.dto.MessageHistoryDto;
import com.costrategix.chat.exception.MessageException;
import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.MessageAttachment;
import com.costrategix.chat.model.MessageRecipients;
import com.costrategix.chat.repository.MessageAttachmentRepository;
import com.costrategix.chat.repository.MessageRecipientRepository;
import com.costrategix.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private MessageRecipientRepository messageRecipientRepository;
    private MessageAttachmentRepository messageAttachmentRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageRecipientRepository messageRecipientRepository, MessageAttachmentRepository messageAttachmentRepository) {
        this.messageRepository = messageRepository;
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageAttachmentRepository = messageAttachmentRepository;
    }

    public Message saveMessage(Message message, long fromId, long recipientId) {
        Message newMessage = new Message(); // we can use constructor here but to validate in future we used getters and setters
        newMessage.setSubject(message.getSubject());
        newMessage.setContent(message.getContent());
        newMessage.setFromId(fromId);
        newMessage.setThreadId(message.getThreadId());
        newMessage = this.messageRepository.save(newMessage);
        if (newMessage.getId() != 0) {
            this.saveMessageRecipientData(newMessage.getId(), recipientId);
            this.saveAttachmentByMessageId(newMessage.getId(), "abc.png");
        }
        return newMessage;
    }

    public MessageRecipients saveMessageRecipientData(long messageId, long toId) {
        MessageRecipients result = new MessageRecipients();
        result.setMessageId(messageId);
        result.setRecipientId(toId);
        return this.messageRecipientRepository.save(result);
    }

    public MessageAttachment saveAttachmentByMessageId(long messageId, String fileName) {
        MessageAttachment attachment = new MessageAttachment();
        attachment.setMessageId(messageId);
        attachment.setFileName(fileName);
        return this.messageAttachmentRepository.save(attachment);
    }

    public Message getMessageById(long messageId) throws MessageException {
        return this.messageRepository.findById(messageId).orElseThrow(() -> new MessageException(messageId));
    }

    public boolean updateReadStatusByMessageId(long messageId) {
        return (this.messageRepository.updateMessageByMessageId(messageId) > 0) ? true : false;
    }

    public List<MessageHistoryDto> getMessageHistoryByUserId(long userId) {
        return this.messageRepository.getMessageHistoryByUserId(userId);
    }
}
