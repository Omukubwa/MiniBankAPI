package com.compulynx.banking.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceUpdateRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String transactionType;
}
