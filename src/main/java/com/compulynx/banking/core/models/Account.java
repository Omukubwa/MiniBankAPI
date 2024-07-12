package com.compulynx.banking.core.models;

import com.compulynx.banking.auth.models.Auditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/*
*Account class contains the very basic details of a customer's account.
* It Extends Auditing abstract class which I created in package com.compulynx.banking.auth.models
* This Auditing Class contains fields that can be used to track the changes made on an account
*/

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String accountName;
    private BigDecimal accountBalance;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
}
