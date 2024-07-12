package com.compulynx.banking.core.services;

import com.compulynx.banking.auth.services.AuthenticationService;
import com.compulynx.banking.core.models.BalanceUpdateRequest;
import com.compulynx.banking.core.models.Transaction;
import com.compulynx.banking.core.models.TransactionRequest;
import com.compulynx.banking.core.repositories.TransactionsRepository;
import com.compulynx.banking.utils.ResponseMessage;
import com.compulynx.banking.utils.UtilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Value("${office.account}")
    private String officeAccount;

    @Value("${optional.credit.accounts}")
    private String creditAccounts;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AuthenticationService customersService;

    public ResponseMessage CashDepositTransaction(TransactionRequest req) {
        var res = new ResponseMessage();
        //Validate account
        var account = accountsService.getAccountDetails(req.getAccountNumber());
        if (account != null) {
            //Validate Customer Id
            var customer = customersService.SearchByCustomerId(req.getCustomerId());
            if (customer != null) {
                var balanceRequest = new BalanceUpdateRequest();
                balanceRequest.setTransactionType("CREDIT");
                balanceRequest.setAmount(req.getAmount());
                balanceRequest.setAccountNumber(req.getAccountNumber());
                //Update Balance
                accountsService.UpdateAccountBalance(balanceRequest);

                //Generate Transaction Code
                var tranCode = utilityService.generateTransactionCode(8);

                //Generate 2 Transactions Debit Transaction (Debit Office Account) && Credit Transaction (Credit Customer Account)
                //Debit
                var dr_transaction = new Transaction();
                dr_transaction.setTransactionAmount(req.getAmount());
                dr_transaction.setTransactionCode(tranCode);
                dr_transaction.setDrCr("Dr");
                dr_transaction.setAccountNumber(officeAccount);
                dr_transaction.setTransactionType("Deposit");
                account.getTransactions().add(dr_transaction);

                //Credit
                var cr_transaction = new Transaction();
                cr_transaction.setTransactionAmount(req.getAmount());
                cr_transaction.setTransactionCode(tranCode);
                cr_transaction.setAccountNumber(req.getAccountNumber());
                cr_transaction.setTransactionType("Deposit");
                cr_transaction.setDrCr("Cr");
                account.getTransactions().add(cr_transaction);

                //Update Account Transactions
                accountsService.SaveTransactions(account);
                res.setStatus(true);
                //Ordering Transactions in descending order
                var acctData = accountsService.getAccountDetails(req.getAccountNumber());
                var sortedTransactions = acctData.getTransactions().stream()
                        .sorted(Comparator.comparingLong(Transaction::getId).reversed())
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                acctData.setTransactions(sortedTransactions);
                res.setEntity(acctData);
                res.setMessage("Successful");
            } else {
                res.setMessage("Customer Does not exist!");
                res.setStatus(false);
            }
        } else {
            res.setMessage("Account Does not exist!");
            res.setStatus(false);
        }
        return res;
    }

    public ResponseMessage CashWithdrawalTransaction(TransactionRequest req) {
        var res = new ResponseMessage();
        //Validate account
        var account = accountsService.getAccountDetails(req.getAccountNumber());
        if (account != null) {
            //Check Account Balance
            if (account.getAccountBalance().compareTo(BigDecimal.ZERO) <= 0) {
                res.setMessage("Account Balance is Low! Kindly deposit first!");
                res.setStatus(false);
            } else {
                //Validate Customer Id
                var customer = customersService.SearchByCustomerId(req.getCustomerId());
                if (customer != null) {
                    var balanceRequest = new BalanceUpdateRequest();
                    balanceRequest.setTransactionType("DEBIT");
                    balanceRequest.setAmount(req.getAmount());
                    balanceRequest.setAccountNumber(req.getAccountNumber());
                    //Update Balance
                    accountsService.UpdateAccountBalance(balanceRequest);

                    //Generate Transaction Code
                    var tranCode = utilityService.generateTransactionCode(8);

                    //Generate 2 Transactions Debit Transaction (Debit Customer Account) && Credit Transaction (Credit Office Account)
                    //Debit
                    var dr_transaction = new Transaction();
                    dr_transaction.setTransactionAmount(req.getAmount());
                    dr_transaction.setTransactionCode(tranCode);
                    dr_transaction.setTransactionType("Withdrawal");
                    dr_transaction.setDrCr("Dr");
                    dr_transaction.setAccountNumber(req.getAccountNumber());
                    account.getTransactions().add(dr_transaction);

                    //Credit
                    var cr_transaction = new Transaction();
                    cr_transaction.setTransactionAmount(req.getAmount());
                    cr_transaction.setTransactionCode(tranCode);
                    cr_transaction.setTransactionType("Withdrawal");
                    cr_transaction.setAccountNumber(officeAccount);
                    cr_transaction.setDrCr("Cr");
                    account.getTransactions().add(cr_transaction);

                    //Update Account Transactions
                    accountsService.SaveTransactions(account);

                    res.setStatus(true);
                    //Ordering Transactions in descending order
                    var acctData = accountsService.getAccountDetails(req.getAccountNumber());
                    var sortedTransactions = acctData.getTransactions().stream()
                            .sorted(Comparator.comparingLong(Transaction::getId).reversed())
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    acctData.setTransactions(sortedTransactions);
                    res.setEntity(acctData);
                    res.setMessage("Successful");
                } else {
                    res.setMessage("Customer Does not exist!");
                    res.setStatus(false);
                }
            }
        } else {
            res.setMessage("Account Does not exist!");
            res.setStatus(false);
        }
        return res;
    }

    public ResponseMessage FundsTransferTransaction(TransactionRequest req) {
        var res = new ResponseMessage();
        //Validate account
        var account = accountsService.getAccountDetails(req.getAccountNumber());
        if (account != null) {
            //Check Account Balance
            if (account.getAccountBalance().compareTo(BigDecimal.ZERO) <= 0) {
                res.setMessage("Account Balance is Low! Kindly deposit first!");
                res.setStatus(false);
            } else {
                //Validate Customer Id
                var customer = customersService.SearchByCustomerId(req.getCustomerId());
                if (customer != null) {
                    var balanceRequest = new BalanceUpdateRequest();
                    balanceRequest.setTransactionType("DEBIT");
                    balanceRequest.setAmount(req.getAmount());
                    balanceRequest.setAccountNumber(req.getAccountNumber());
                    //Update Balance
                    accountsService.UpdateAccountBalance(balanceRequest);

                    //Generate Transaction Code
                    var tranCode = utilityService.generateTransactionCode(8);

                    //Generate 2 Transactions Debit Transaction (Debit Customer Account) && Credit Transaction (Credit Office Account)
                    //Debit
                    var dr_transaction = new Transaction();
                    dr_transaction.setTransactionAmount(req.getAmount());
                    dr_transaction.setTransactionCode(tranCode);
                    dr_transaction.setTransactionType("Funds Transfer");
                    dr_transaction.setDrCr("Dr");
                    dr_transaction.setAccountNumber(req.getAccountNumber());
                    account.getTransactions().add(dr_transaction);

                    //Credit
                    var cr_transaction = new Transaction();
                    cr_transaction.setTransactionAmount(req.getAmount());
                    cr_transaction.setTransactionCode(tranCode);
                    cr_transaction.setTransactionType("Funds Transfer");
                    cr_transaction.setAccountNumber(req.getCreditAccountNumber());
                    cr_transaction.setDrCr("Cr");
                    account.getTransactions().add(cr_transaction);

                    //Update Account Transactions
                    accountsService.SaveTransactions(account);

                    res.setStatus(true);
                    //Ordering Transactions in descending order
                    var acctData = accountsService.getAccountDetails(req.getAccountNumber());
                    var sortedTransactions = acctData.getTransactions().stream()
                            .sorted(Comparator.comparingLong(Transaction::getId).reversed())
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    acctData.setTransactions(sortedTransactions);
                    res.setEntity(acctData);
                    res.setMessage("Successful");
                } else {
                    res.setMessage("Customer Does not exist!");
                    res.setStatus(false);
                }
            }
        } else {
            res.setMessage("Account Does not exist!");
            res.setStatus(false);
        }
        return res;
    }

    public List<String> getAvailableCreditAccounts() {
        return Arrays.stream(creditAccounts.split("\\s*,\\s*"))
                .collect(Collectors.toList());
    }

    public List<Transaction> SearchByTransactionCode(String tranCode) {
        return transactionsRepository.findByTransactionCode(tranCode);
    }
}
