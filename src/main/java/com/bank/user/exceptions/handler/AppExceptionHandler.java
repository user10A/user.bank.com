package com.bank.user.exceptions.handler;

import com.bank.user.exceptions.AlreadyExistsException;
import com.bank.user.exceptions.ExceptionResponse;
import com.bank.user.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AppExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistsException(AlreadyExistsException e){
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e){
        log.warn("NotFoundException: -------------------- {}", e.getMessage());
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse HandleConflictException(HttpClientErrorException.Conflict e) {
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ExceptionResponse.builder()
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.SEE_OTHER)
                .message(e.getMessage())
                .build();
    }

}
