package com.mo.bank.controllers;

import com.mo.bank.entities.Account;
import com.mo.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Account account) throws IllegalAccessException {
        accountService.createAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        return accountService.authenticateAccount(request);
    }

    @PostMapping("/generate-card")
    public ResponseEntity<String> generateCard(@RequestBody Map<String, String> request) {
        return accountService.createCreditCard(request);
    }

    @PostMapping("/generate-card/password")
    public ResponseEntity<String> generateCardPassword(@RequestBody String email, @RequestBody String password) {
        return accountService.createCardPassword(email, password);
    }

    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(@RequestBody String email, @RequestBody Boolean status) {
        return accountService.changeAccountStatus(email, status);
    }

    @PutMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody String email, @RequestBody Double money) {
        return accountService.deposit(email, money);
    }
    @PutMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody String email, @RequestBody Double money) {
        return accountService.withdraw(email, money);
    }

}
