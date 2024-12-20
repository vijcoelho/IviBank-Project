package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.repositories.AccountRepository;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Getter
    private Account authAccount;

    public AuthService(
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
        authAccount = accountRepository.findByEmail(email)
                .orElseThrow();

        return authAccount;
    }

    public Account changeEmailPassword(Map<String, String> input) {
        String email = input.get("email");
        String newPassword = input.get("newPassword");
        String confirmPassword = input.get("confirmPassword");

        Optional<Account> currentAccount = accountRepository.findByEmail(email);

        if (currentAccount.isEmpty()) {
            return null;
        }
        if (!newPassword.equals(confirmPassword)) {
            return null;
        }

        Account account = currentAccount.get();
        account.setEmailPassword(passwordEncoder.encode(newPassword));
        return accountRepository.save(account);
    }
}
