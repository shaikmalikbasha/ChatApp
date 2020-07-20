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
    private long recipientId;
    private boolean isRead;
    private String fileName;
    private List<MessageHistoryDto> childs = new ArrayList<>();

    public MessageHistoryDto() {
    }

    public MessageHistoryDto(long messageId, String subject, String content, long senderId, Long threadId, long recipientId, boolean isRead, String fileName) {
        this.messageId = messageId;
        this.subject = subject;
        this.content = content;
        this.senderId = senderId;
        this.threadId = threadId;
        this.recipientId = recipientId;
        this.isRead = isRead;
        this.fileName = fileName;
    }

    public MessageHistoryDto(long messageId, String subject, String content, long senderId, Long threadId, long recipientId, boolean isRead, String fileName, List<MessageHistoryDto> childs) {
        this.messageId = messageId;
        this.subject = subject;
        this.content = content;
        this.senderId = senderId;
        this.threadId = threadId;
        this.recipientId = recipientId;
        this.isRead = isRead;
        this.fileName = fileName;
        this.childs = childs;
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

    public List<MessageHistoryDto> getChilds() {
        return childs;
    }

    public void setChilds(List<MessageHistoryDto> childs) {
        this.childs = childs;
    }

    @Override
    public String toString() {
        return "\n{" +
                "childs=" + childs +
                ", messageId=" + messageId +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
                ", threadId=" + threadId +
                ", recipientId=" + recipientId +
                ", isRead=" + isRead +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
