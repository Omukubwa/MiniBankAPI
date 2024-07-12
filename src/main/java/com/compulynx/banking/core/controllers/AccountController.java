package com.compulynx.banking.core.controllers;

import com.compulynx.banking.core.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Account")
@CrossOrigin(origins = "*")
public class AccountController {
    @Autowired
    private AccountsService accountsService;

    @GetMapping("/Balance")
    public ResponseEntity<?> AccountDetails(@RequestParam String accountNo) {
        var res = accountsService.getAccountDetails(accountNo);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/MiniStatement")
    public ResponseEntity<?> MiniStatement(@RequestParam String accountNo) {
        var res = accountsService.CustomerMiniStatement(accountNo);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/FullStatement")
    public ResponseEntity<?> FullStatement(@RequestParam String accountNo) {
        var res = accountsService.CustomerFullStatement(accountNo);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }
}
