package com.bank.user.dto;

import java.math.BigDecimal;

public record TransactionRequest(String accountNumber, BigDecimal amount) {

    public TransactionRequest {
        if (accountNumber == null || !accountNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Номер счёта должен содержать ровно 10 цифр.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля.");
        }
    }
}
