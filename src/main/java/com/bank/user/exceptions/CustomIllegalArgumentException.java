package com.bank.user.exceptions;

import lombok.Getter;

@Getter
public class CustomIllegalArgumentException extends IllegalArgumentException{
    private final String messageCode;
    private final Object[] args;

    public CustomIllegalArgumentException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public CustomIllegalArgumentException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}
