package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.repositories.AccountRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME_MS = 30_000;
    private final Map<String, AttemptInfo> attempts = new HashMap<>();

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

    public ResponseEntity<?> login(Map<String, String> input) {
        String email = input.get("email");
        String password = input.get("password");

        long remainingTime = getRemainingBlockTime(email);
        if (remainingTime > 0) {
            String message = String.format(
                    "Account blocked. Try again in %d seconds",
                    remainingTime / 1000
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            attempts.remove(email);
            Account authAccount = accountRepository.findByEmail(email)
                    .orElseThrow();
            return ResponseEntity.ok(authAccount);

        } catch (Exception e) {
            registerFailedAttempt(email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Password is incorrect");
        }
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

    private long getRemainingBlockTime(String email) {
        AttemptInfo info = attempts.get(email);
        if (info != null && info.attempts >= MAX_ATTEMPTS) {
            long elapsedTime = System.currentTimeMillis() - info.blockTime;
            if (elapsedTime < BLOCK_TIME_MS) {
                return BLOCK_TIME_MS - elapsedTime;
            }
        }
        return 0;
    }

    private boolean isBlocked(String email) {
        AttemptInfo info = attempts.get(email);
        if (info != null && info.attempts >= MAX_ATTEMPTS) {
            if (System.currentTimeMillis() - info.blockTime < BLOCK_TIME_MS) {
                return true;
            } else {
                attempts.remove(email);
            }
        }
        return false;
    }

    private void registerFailedAttempt(String email) {
        AttemptInfo info = attempts.getOrDefault(email, new AttemptInfo());
        info.attempts++;
        if (info.attempts >= MAX_ATTEMPTS) {
            info.blockTime = System.currentTimeMillis();
        }
        attempts.put(email, info);
    }

    private static class AttemptInfo {
        int attempts = 0;
        long blockTime = 0;
    }
}
