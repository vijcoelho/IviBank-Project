package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.RoleName;
import com.mo.bank.entities.Roles;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.security.JwtTokenService;
import com.mo.bank.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfiguration securityConfiguration;


    public void createAccount(Account account) throws IllegalAccessException {
        if (account.getEmail() == null || account.getEmailPassword() == null) {
            throw new IllegalAccessException("Email and password is required");
        }
        if (account.getRoles() == null || account.getRoles().isEmpty()) {
            Roles defaultRole = new Roles();
            defaultRole.setName(RoleName.ROLE_CUSTOMER);
            account.setRoles(List.of(defaultRole));
        }

        account.setEmailPassword(securityConfiguration.passwordEncoder().encode(account.getEmailPassword()));
        accountRepository.save(account);
    }

    public ResponseEntity<String> authenticateAccount(Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            if (email.equals(account.get().getEmail()) && securityConfiguration.passwordEncoder().matches(password, account.get().getEmailPassword())) {
                String token = jwtTokenService.generateToken(account);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
