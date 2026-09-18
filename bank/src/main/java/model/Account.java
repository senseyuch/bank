package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Account {

    Long accountId;
    String accountNumber;
    String accountHolderName;
    BigDecimal balance;
    LocalDateTime createdAt;

    public Account(Long accountId, String accountHolderName, BigDecimal balance, LocalDateTime createdAt) {

    }

    @Override
    public String toString() {
        return String.format(
                "Account{id=%d, number='%s', name='%s', balance=%.2f}",
                accountId, accountNumber, accountHolderName, balance
        );
    }
}
