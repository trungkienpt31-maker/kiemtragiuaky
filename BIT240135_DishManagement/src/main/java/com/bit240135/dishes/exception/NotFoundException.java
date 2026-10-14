package com.bit240135.dishes.exception;

public class NotFoundException extends RuntimeException {
    private final String messageKey;

    public NotFoundException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
