package com.costrategix.chat.dto;

import org.springframework.stereotype.Component;

@Component
public class UserDto {
    private long recipientId;
    private String recipientName;
    private String recipientEmail;

    public UserDto() {
    }

    public UserDto(long recipientId, String recipientName, String recipientEmail) {
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}
