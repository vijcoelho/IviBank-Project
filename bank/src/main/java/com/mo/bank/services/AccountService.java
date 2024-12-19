package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AccountService(
            AccountRepository accountRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account signup(Account account) {
        account.setEmailPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account login(Map<String, String> input) {
        String email = input.get("email");
        String password = input.get("password");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        return accountRepository.findByEmail(email)
                .orElseThrow();
    }

    public List<Account> allAccounts() {
        List<Account> accountList = new ArrayList<>(accountRepository.findAll());
        return accountList;
    }
}
