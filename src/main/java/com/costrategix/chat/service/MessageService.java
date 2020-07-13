package com.costrategix.chat.service;

import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.MessageRecipients;
import com.costrategix.chat.repository.MessageRecipientRepository;
import com.costrategix.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private MessageRecipientRepository messageRecipientRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageRecipientRepository messageRecipientRepository) {
        this.messageRepository = messageRepository;
        this.messageRecipientRepository = messageRecipientRepository;
    }

    public Message saveMessage(Message message, long fromId) {
        Message newMessage = new Message();
        newMessage.setSubject(message.getSubject());
        newMessage.setContent(message.getContent());
        newMessage.setFromId(fromId);
        newMessage.setToId(message.getToId());
        newMessage = this.messageRepository.save(newMessage);
        if (newMessage.getId() != 0) {
            this.saveMessageRecipientData(newMessage.getId(), newMessage.getFromId());
        }
        return newMessage;
    }

    public MessageRecipients saveMessageRecipientData(long messageId, long fromId) {
        MessageRecipients result = new MessageRecipients();
        result.setMessageId(messageId);
        result.setRecipientId(fromId);
        return this.messageRecipientRepository.save(result);
    }


}
