package com.costrategix.chat.exception;


public class MessageException extends Exception {
    private long messageId;
    private String message;

    public MessageException(long messageId) {
        super(String.format("Message was not found with id: ", messageId));
    }

    public MessageException(String message) {
        super(message);
    }
}
