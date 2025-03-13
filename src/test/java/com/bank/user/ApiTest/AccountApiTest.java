package com.bank.user.ApiTest;

import com.bank.user.api.AccountApi;
import com.bank.user.dto.*;
import com.bank.user.exceptions.NotFoundException;
import com.bank.user.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountApi.class)
class AccountApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешное создание аккаунта")
    void testCreateAccount_Success() throws Exception {
        AccountRequest accountRequest = new AccountRequest(1L, "1234567891");
        SimpleResponse simpleResponse = new SimpleResponse(HttpStatus.CREATED, "Счёт успешно создан.");

        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(simpleResponse);

        mockMvc.perform(post("/api/v3/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Счёт успешно создан."))
                .andExpect(jsonPath("$.httpStatus").value("CREATED"));

        verify(accountService, times(1)).createAccount(any(AccountRequest.class));
    }

    @Test
    @DisplayName("Создание аккаунта: пользователь не найден")
    void testCreateAccount_UserNotFound() throws Exception {
        long userId = 99L;
        AccountRequest accountRequest = new AccountRequest(userId, "1234567891");

        when(accountService.createAccount(any(AccountRequest.class)))
                .thenThrow(new NotFoundException("Пользователь с ID " + userId + " не найден."));

        mockMvc.perform(post("/api/v3/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с ID " + userId + " не найден."));

        verify(accountService, times(1)).createAccount(any(AccountRequest.class));
    }

    @Test
    @DisplayName("Успешное пополнение счёта")
    void testDeposit_Success() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("1234567891", BigDecimal.valueOf(100));
        SimpleResponse simpleResponse = new SimpleResponse(HttpStatus.OK, "Счёт успешно пополнен.");

        when(accountService.deposit(any(TransactionRequest.class))).thenReturn(simpleResponse);

        mockMvc.perform(put("/api/v3/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Счёт успешно пополнен."));

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Неуспешное пополнение счёта: счёт не найден")
    void testDeposit_AccountNotFound() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("1234567891", BigDecimal.valueOf(100));

        when(accountService.deposit(any(TransactionRequest.class)))
                .thenThrow(new NotFoundException("Счёт с таким номером : " + transactionRequest.accountNumber() + " не найден"));

        mockMvc.perform(put("/api/v3/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isNotFound()) // Проверяем что статус ответа 404
                .andExpect(jsonPath("$.message").value("Счёт с таким номером : " + transactionRequest.accountNumber() + " не найден"));

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }


    @Test
    @DisplayName("Неуспешное пополнение счёта: сумма меньше или равна 0")
    void testDeposit_ZeroOrNegativeAmount() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("1234567891", BigDecimal.valueOf(-50));

        when(accountService.deposit(any(TransactionRequest.class)))
                .thenThrow(new IllegalArgumentException("Сумма должна быть больше нуля."));

        mockMvc.perform(put("/api/v3/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest()) // Проверяем что статус ответа 400
                .andExpect(jsonPath("$.message").value("Сумма должна быть больше нуля."));

        verify(accountService, times(1)).deposit(any(TransactionRequest.class));
    }




    @Test
    @DisplayName("Успешное снятие средств")
    void testWithdraw_Success() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("1234567891", BigDecimal.valueOf(50));
        SimpleResponse simpleResponse = new SimpleResponse(HttpStatus.OK, "Списание выполнено.");

        when(accountService.withdraw(any(TransactionRequest.class))).thenReturn(simpleResponse);

        mockMvc.perform(put("/api/v3/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Списание выполнено."));

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Неуспешное снятие средств: счёт не найден")
    void testWithdraw_AccountNotFound() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("1234567891", BigDecimal.valueOf(50));

        when(accountService.withdraw(any(TransactionRequest.class)))
                .thenThrow(new NotFoundException("Счёт с таким номером : " + transactionRequest.accountNumber() + " не найден"));

        mockMvc.perform(put("/api/v3/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isNotFound()) // Проверяем что статус ответа 404
                .andExpect(jsonPath("$.message").value("Счёт с таким номером : " + transactionRequest.accountNumber() + " не найден"));

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Неуспешное снятие средств: сумма меньше или равна 0")
    void testWithdraw_ZeroOrNegativeAmount() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest("1234567891", BigDecimal.valueOf(-50));

        when(accountService.withdraw(any(TransactionRequest.class)))
                .thenThrow(new IllegalArgumentException("Сумма должна быть больше нуля."));

        mockMvc.perform(put("/api/v3/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest()) // Проверяем что статус ответа 400
                .andExpect(jsonPath("$.message").value("Сумма должна быть больше нуля."));

        verify(accountService, times(1)).withdraw(any(TransactionRequest.class));
    }

    @Test
    @DisplayName("Успешное получение счетов пользователя")
    void testGetAccountsByUserId_Success() throws Exception {
        long userId = 1L;
        long accountId = 1L;
        UserResponse userResponse = new UserResponse(
                userId,
                "Джон Уик",
                "джон@example.com",
                LocalDateTime.now(),
                List.of(new AccountResponse(accountId, "1234567891", BigDecimal.valueOf(100)))
        );

        when(accountService.getAccountsByUserId(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v3/accounts/get/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.userName").value("Джон Уик"))
                .andExpect(jsonPath("$.accounts[0].id").value(accountId))
                .andExpect(jsonPath("$.accounts[0].accountNumber").value("1234567891"))
                .andExpect(jsonPath("$.accounts[0].balance").value(100));

        verify(accountService, times(1)).getAccountsByUserId(userId);
    }

    @Test
    @DisplayName("Получение счетов пользователя: пользователь не найден")
    void testGetAccountsByUserId_UserNotFound() throws Exception {
        long userId = 99L;

        when(accountService.getAccountsByUserId(userId))
                .thenThrow(new NotFoundException("Пользователь с ID " + userId + " не найден."));

        mockMvc.perform(get("/api/v3/accounts/get/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с ID " + userId + " не найден."));

        verify(accountService, times(1)).getAccountsByUserId(userId);
    }
}
