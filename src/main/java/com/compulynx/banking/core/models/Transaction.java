package com.compulynx.banking.core.models;

import com.compulynx.banking.auth.models.Auditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/*
 *Transaction class contains the very basic details of transactions done by a customer in each account.
 * It Extends Auditing abstract class which I created in package com.compulynx.banking.auth.models
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Transactions")
public class Transaction extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal transactionAmount;
    private String accountNumber;
    private String drCr;
    private String transactionType;
    private String transactionCode;
}
