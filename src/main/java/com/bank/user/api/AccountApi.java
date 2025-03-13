package com.bank.user.api;

import com.bank.user.dto.AccountRequest;
import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.TransactionRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("api/v3/accounts"))
@CrossOrigin
@Tag(name = "Accounts api", description = "APIs for Accounts")
@RequiredArgsConstructor
public class AccountApi {
    private final AccountService accountService;

    @PostMapping("/create")
    @Operation(
            summary = "Создание нового аккаунта",
            description = "Метод создает новый аккаунт на основе предоставленных данных AccountRequest."
    )
    public ResponseEntity<SimpleResponse> createAccount(@Valid @RequestBody AccountRequest account) {
        SimpleResponse response = accountService.createAccount(account);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PutMapping("/deposit")
    @Operation(
            summary = "Пополнение баланса",
            description = "Метод добавляет указанную сумму на баланс указанного аккаунта."
    )
    public ResponseEntity<SimpleResponse> deposit(@Valid @RequestBody TransactionRequest transactionRequest) {
        SimpleResponse response = accountService.deposit(transactionRequest);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PutMapping("/withdraw")
    @Operation(
            summary = "Снятие средств",
            description = "Метод снимает указанную сумму с баланса указанного аккаунта."
    )
    public ResponseEntity<SimpleResponse> withdraw(@Valid @RequestBody TransactionRequest transactionRequest) {
        SimpleResponse response = accountService.withdraw(transactionRequest);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @GetMapping("/get/{userId}")
    @Operation(
            summary = "Получение счетов пользователя",
            description = "Метод выводит все счета пользователя по его уникальному идентификатору (ID)"
    )
    public UserResponse getAccountsByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId);
    }
}
