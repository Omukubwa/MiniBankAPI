package com.compulynx.banking.core.services;

import com.compulynx.banking.core.models.Account;
import com.compulynx.banking.core.models.BalanceUpdateRequest;
import com.compulynx.banking.core.models.Transaction;
import com.compulynx.banking.core.repositories.AccountsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountsService {
    @Autowired
    private AccountsRepository accountsRepository;

    public Account getAccountDetails(String accountNumber){
        var account = accountsRepository.findByAccountNumber(accountNumber);
        return account.orElse(null);
    }

    public Set<Transaction> CustomerMiniStatement(String accountNo) {
        var acctData = accountsRepository.findByAccountNumber(accountNo);
        return acctData.map(account -> account.getTransactions().stream()
                .sorted(Comparator.comparingLong(Transaction::getId).reversed())
                .limit(10)
                .collect(Collectors.toCollection(LinkedHashSet::new))).orElse(null);
    }

    public Set<Transaction> CustomerFullStatement(String accountNo) {
        var acctData = accountsRepository.findByAccountNumber(accountNo);
        return acctData.map(account -> account.getTransactions().stream()
                .sorted(Comparator.comparingLong(Transaction::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new))).orElse(null);
    }

    public void SaveTransactions(Account account){
        accountsRepository.save(account);
    }
    public void UpdateAccountBalance(BalanceUpdateRequest req){
        var account = accountsRepository.findByAccountNumber(req.getAccountNumber());
        if(account.isPresent()){
            Account currentAccount = account.get();
            BigDecimal newBalance = currentAccount.getAccountBalance().add(req.getAmount());
            if(req.getTransactionType().toUpperCase().contains("CREDIT")){
                account.get().setAccountBalance(newBalance);
            }
            else if(req.getTransactionType().toUpperCase().contains("DEBIT")){
                newBalance = currentAccount.getAccountBalance().subtract(req.getAmount());
                account.get().setAccountBalance(newBalance);
            }
            currentAccount.setModifiedOn(new Date());
            accountsRepository.save(currentAccount);
        }
    }
}
