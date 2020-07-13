package com.costrategix.chat.model;

import javax.persistence.*;

@Entity
@Table(name = "message_recipients")
public class MessageRecipients {
    @Column(name = "recipientReadStatus")
    boolean isRead = false;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "messageId")
    private long messageId;
    @Column(name = "recipientId")
    private long recipientId;

    public MessageRecipients() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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

    @Override
    public String toString() {
        return "MessageRecipients{" +
                "isRead=" + isRead +
                ", id=" + id +
                ", messageId=" + messageId +
                ", recipientId=" + recipientId +
                '}';
    }
}
