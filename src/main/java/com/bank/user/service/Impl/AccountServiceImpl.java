package com.bank.user.service.Impl;

import com.bank.user.dto.AccountRequest;
import com.bank.user.dto.AccountResponse;
import com.bank.user.dto.SimpleResponse;
import com.bank.user.dto.TransactionRequest;
import com.bank.user.dto.UserResponse;
import com.bank.user.entities.Account;
import com.bank.user.entities.User;
import com.bank.user.exceptions.NotFoundException;
import com.bank.user.repo.AccountRepo;
import com.bank.user.repo.UserRepo;
import com.bank.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public SimpleResponse createAccount(AccountRequest accountRequest) {
        try {
            if (accountRequest == null) {
                log.warn("Попытка создать счёт с пустым запросом.");
                throw new IllegalArgumentException("Ошибка: запрос на создание счёта пустой.");
            }

            User user = userRepo.findById(accountRequest.getUserId()).orElseThrow(
                    () -> new NotFoundException("Пользователь с ID " + accountRequest.getUserId() + " не найден."));

            Account account = Account.builder()
                    .accountNumber(accountRequest.getAccountNumber())
                    .user(user)
                    .balance(BigDecimal.ZERO)
                    .build();

            accountRepo.save(account);
            log.info("Счёт успешно создан. Номер счёта: {}, ID пользователя: {}", accountRequest.getAccountNumber(), accountRequest.getUserId());
            return new SimpleResponse(HttpStatus.CREATED, "Счёт успешно создан.");
        } catch (NotFoundException | IllegalArgumentException e) {
            log.error("Ошибка при создании счёта: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при создании счёта: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при создании счёта", e);
        }
    }

    @Override
    @Transactional
    public SimpleResponse deposit(TransactionRequest request) {
        try {
            validateAmount(request.amount());
            Account account = accountRepo.getAccountByAccountNumber(request.accountNumber())
                    .orElseThrow(() -> new NotFoundException("Счёт с таким номером : " + request.accountNumber() + " не найден"));

            account.deposit(request.amount());
            accountRepo.save(account);

            log.info("Счёт с номером {} успешно пополнен. Текущий баланс: {}", account.getAccountNumber(), account.getBalance());
            return new SimpleResponse(HttpStatus.OK, String.format("Счёт успешно пополнен. Текущий баланс: %s.", account.getBalance()));
        } catch (IllegalArgumentException | NotFoundException e) {
            log.error("Ошибка при пополнении счёта: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при пополнении счёта: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при пополнении счёта", e);
        }
    }

    @Override
    @Transactional
    public SimpleResponse withdraw(TransactionRequest request) {
        try {
            validateAmount(request.amount());
            Account account = accountRepo.getAccountByAccountNumber(request.accountNumber()).orElseThrow(
                    () -> new NotFoundException("Счёт с номером " + request.accountNumber() + " не найден."));

            account.withdraw(request.amount());
            accountRepo.save(account);

            log.info("Списание успешно выполнено. Текущий баланс: {}", account.getBalance());
            return new SimpleResponse(HttpStatus.OK, String.format("Списание выполнено. Текущий баланс: %s.", account.getBalance()));
        } catch (IllegalArgumentException | NotFoundException e) {
            log.error("Ошибка при списании со счёта: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при списании со счёта: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при списании со счёта", e);
        }
    }

    @Override
    public UserResponse getAccountsByUserId(Long userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(
                    () -> {
                        log.warn("Пользователь с ID {} не найден.", userId);
                        return new NotFoundException("Пользователь с ID " + userId + " не найден.");
                    }
            );
            List<AccountResponse> accountResponses = accountRepo.getAccountsByUserId(userId);
            if (accountResponses.isEmpty()) {
                log.warn("У пользователя с ID {} нет активных счетов.", userId);
                throw new NotFoundException("У пользователя с ID " + userId + " нет активных счетов.");
            }
            log.info("Получение счетов пользователя по его ID. Количество счетов: {}", accountResponses.size());
            return new UserResponse(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getCreationDate(),
                    accountResponses
            );
        } catch (NotFoundException e) {
            log.error("Ошибка при получении счетов пользователя: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при получении счетов пользователя: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении счетов пользователя", e);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Некорректная сумма: {}", amount);
            throw new IllegalArgumentException("Сумма должна быть больше нуля.");
        }
    }
}
