package com.costrategix.chat.exception;


public class MessageException extends Exception {
    private long messageId;

    public MessageException(long messageId) {
        super(String.format("Message was not found with id: ", messageId));
    }
}
