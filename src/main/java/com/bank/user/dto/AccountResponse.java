package com.bank.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
}
