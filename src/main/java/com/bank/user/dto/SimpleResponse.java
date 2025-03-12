package com.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleResponse {
  
    private String message;
    private HttpStatus httpStatus;


    public SimpleResponse(HttpStatus httpStatus, String successMessage) {
        this.httpStatus=httpStatus;
        this.message=successMessage;
    }
    public SimpleResponse(String successMessage, HttpStatus httpStatus) {
        this.httpStatus=httpStatus;
        this.message=successMessage;
    }
}