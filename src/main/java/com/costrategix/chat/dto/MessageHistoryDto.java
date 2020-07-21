package com.costrategix.chat.dto;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageHistoryDto {
    private long messageId;
    private String subject;
    private String content;
    private long senderId;
    private Long threadId;
    private List<UserDto> recipients = new ArrayList<>();
    private boolean isRead;
    private String fileName;
    private List<MessageHistoryDto> repliedMessages = new ArrayList<>();

    public MessageHistoryDto() {
    }

    public MessageHistoryDto(long messageId, String subject, String content, long senderId, Long threadId, boolean isRead, String fileName) {
        this.messageId = messageId;
        this.subject = subject;
        this.content = content;
        this.senderId = senderId;
        this.threadId = threadId;
        this.isRead = isRead;
        this.fileName = fileName;
    }

    public MessageHistoryDto(long messageId, String subject, String content, long senderId, Long threadId, List<UserDto> recipients, boolean isRead, String fileName, List<MessageHistoryDto> repliedMessages) {
        this.messageId = messageId;
        this.subject = subject;
        this.content = content;
        this.senderId = senderId;
        this.threadId = threadId;
        this.recipients = recipients;
        this.isRead = isRead;
        this.fileName = fileName;
        this.repliedMessages = repliedMessages;
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

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
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

    public List<MessageHistoryDto> getRepliedMessages() {
        return repliedMessages;
    }

    public void setRepliedMessages(List<MessageHistoryDto> repliedMessages) {
        this.repliedMessages = repliedMessages;
    }

    public List<UserDto> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<UserDto> recipients) {
        this.recipients = recipients;
    }
}
