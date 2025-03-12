package com.bank.user.entities;

import com.bank.user.exceptions.CustomIllegalArgumentException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "acc_gen")
    @SequenceGenerator(name = "acc_gen",sequenceName = "acc_seq", allocationSize = 1)
    private Long id;
    private String accountNumber;
    @Column(nullable =false)
    private BigDecimal balance = BigDecimal.ZERO;
    @ManyToOne
    @JsonIgnore
    private User user;

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма списания должна быть положительной");
        }
        if (amount.compareTo(balance) > 0) {
            throw new CustomIllegalArgumentException("Недостаточно средств для списания");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительной");
        }
        this.balance = this.balance.add(amount);
    }

}
