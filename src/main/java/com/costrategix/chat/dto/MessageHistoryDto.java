package com.costrategix.chat.dto;

import org.springframework.stereotype.Component;

@Component
public class MessageHistoryDto {
    private long messageId;
    private String subject;
    private String content;
    private long senderId;
    private long recipientId;
    private boolean isRead;
    private String fileName;

    public MessageHistoryDto() {
    }

    public MessageHistoryDto(long messageId, String subject, String content, long senderId, long recipientId, boolean isRead, String fileName) {
        this.messageId = messageId;
        this.subject = subject;
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.isRead = isRead;
        this.fileName = fileName;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
