package com.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class AccountRequest {
    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "\\d+", message = "Номер счёта должен содержать только цифры")
    @JsonProperty("account_number")
    private String accountNumber;
}
