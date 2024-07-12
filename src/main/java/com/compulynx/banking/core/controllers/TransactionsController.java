package com.compulynx.banking.core.controllers;

import com.compulynx.banking.core.models.TransactionRequest;
import com.compulynx.banking.core.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Transaction")
@CrossOrigin(origins = "*")
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @GetMapping("/Search")
    private ResponseEntity<?> SearchByTransactionCode(@RequestParam("code") String transactionCode){
        return new ResponseEntity<>(transactionsService.SearchByTransactionCode(transactionCode), HttpStatus.OK);
    }

    @GetMapping("/CreditAccounts")
    private ResponseEntity<?> CreditAccounts(){
        return new ResponseEntity<>(transactionsService.getAvailableCreditAccounts(), HttpStatus.OK);
    }

    @PostMapping("/Deposit")
    private ResponseEntity<?> DepositTransaction(@RequestBody TransactionRequest request){
        var res = transactionsService.CashDepositTransaction(request);
        if(res.isStatus()){
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Withdraw")
    private ResponseEntity<?> WithdrawalTransaction(@RequestBody TransactionRequest request){
        var res = transactionsService.CashWithdrawalTransaction(request);
        if(res.isStatus()){
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Transfer")
    private ResponseEntity<?> FundsTransferTransaction(@RequestBody TransactionRequest request){
        var res = transactionsService.FundsTransferTransaction(request);
        if(res.isStatus()){
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
