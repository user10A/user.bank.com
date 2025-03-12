package com.bank.user.service;

import com.bank.user.dto.AccountRequest;
import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.TransactionRequest;
import com.bank.user.dto.UserResponse;

public interface AccountService {
    SimpleResponse createAccount(AccountRequest account);
    SimpleResponse deposit(TransactionRequest request);
    SimpleResponse withdraw(TransactionRequest request);
    UserResponse getAccountsByUserId(Long userId);

}
