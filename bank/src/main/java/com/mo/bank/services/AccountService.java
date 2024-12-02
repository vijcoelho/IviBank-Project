package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<String> createAccount(Account account) {
        return ResponseEntity.ok("Account created \n" + accountRepository.save(account));
    }
}
