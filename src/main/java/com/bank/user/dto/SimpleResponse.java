package com.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleResponse {
  
    private String messageCode;
    private HttpStatus httpStatus;


    public SimpleResponse(HttpStatus httpStatus, String successMessage) {
        this.httpStatus=httpStatus;
        this.messageCode=successMessage;
    }
    public SimpleResponse(String successMessage, HttpStatus httpStatus) {
        this.httpStatus=httpStatus;
        this.messageCode=successMessage;
    }


    public SimpleResponse(boolean b, String verificationCodeCheckedSuccessfully) {
    }
}