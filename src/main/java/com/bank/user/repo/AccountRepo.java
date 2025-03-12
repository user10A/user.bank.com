package com.bank.user.repo;

import com.bank.user.dto.AccountResponse;
import com.bank.user.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    List<AccountResponse> getAccountsByUserId(Long userId);
    Optional<Account> getAccountByAccountNumber(String accountNumber);

}
