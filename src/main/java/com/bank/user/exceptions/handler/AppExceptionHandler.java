package com.bank.user.exceptions.handler;

import com.bank.user.exceptions.AlreadyExistsException;
import com.bank.user.exceptions.ExceptionResponse;
import com.bank.user.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleAlreadyExistsException(AlreadyExistsException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleIllegalStateException(IllegalStateException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleGlobalException(Exception e) {
        return ExceptionResponse.builder()
                .message(e.getMessage())
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ExceptionResponse.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Ошибка чтения запроса: " + e.getMostSpecificCause().getMessage())
                .build();
    }

}


