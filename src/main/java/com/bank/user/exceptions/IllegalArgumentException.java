package com.bank.user.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IllegalArgumentException extends RuntimeException {
    private HttpStatus httpStatus;
    private String exceptionClassName;
    private String message;
}
