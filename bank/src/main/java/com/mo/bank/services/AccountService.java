package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.Card;
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

    private Card card;


    public void createAccount(Account account) throws IllegalAccessException {
        if (account.getEmail() == null || account.getEmailPassword() == null) {
            throw new IllegalAccessException("Email and password is required");
        }
        if (account.getRoles() == null || account.getRoles().isEmpty()) {
            Roles defaultRole = new Roles();
            defaultRole.setName(RoleName.ROLE_CUSTOMER);
            account.setRoles(List.of(defaultRole));
        }
        account.setStatus(true);
        account.setEmailPassword(securityConfiguration.passwordEncoder().encode(account.getEmailPassword()));
        accountRepository.save(account);
    }

    public ResponseEntity<String> authenticateAccount(Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent()) {
            if (email.equals(account.get().getEmail()) &&
                    securityConfiguration.passwordEncoder().matches(password, account.get().getEmailPassword())) {
                String token = jwtTokenService.generateToken(account);
                return ResponseEntity.ok(token);
            }
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    public ResponseEntity<String> createCreditCard(Map<String, String> request) {
        String email = request.get("email");
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent() && account.get().getStatus().equals(true)) {
            account.get().setCreditCardNumber(card.generateCardNumber());
            return ResponseEntity.status(201).body("Card generate");
        }
        return ResponseEntity.badRequest().body("Email does not exist or the account is inactive: Failed to generate card number");
    }

    public ResponseEntity<String> createCardPassword(String email, String password) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent() && account.get().getStatus().equals(true)) {
            account.get().setCardPassword(password);
            return ResponseEntity.status(201).body("Card password created");
        }
        return ResponseEntity.badRequest().body("Email does not exist or the account is inactive: Failed to create card password");
    }

    public ResponseEntity<String> changeAccountStatus(String email, Boolean status) {
        Optional<Account> account = accountRepository.findByEmail(email);
        account.ifPresent(value -> value.setStatus(status));
        return ResponseEntity.ok("New status: " + status);
    }

    public ResponseEntity<String> deposit(String email, Double money) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent() && account.get().getStatus().equals(true)) {
            if (money > 0) {
                Double currentMoney = account.get().getMoney() + money;
                account.get().setMoney(currentMoney);
                accountRepository.save(account.get());
                return ResponseEntity.accepted().body("You made the deposit with success");
            }
        }
        return ResponseEntity.badRequest().body("Email does not exist or the account is inactive: Deposit not made");
    }

    public ResponseEntity<String> withdraw(String email, Double money) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent() && account.get().getStatus().equals(true)) {
            if (money > 0 && account.get().getMoney() >= money) {
                Double currentMoney = account.get().getMoney() - money;
                account.get().setMoney(currentMoney);
                accountRepository.save(account.get());
                return ResponseEntity.accepted().body("You made the withdraw with success");
            }
        }
        return ResponseEntity.badRequest().body("Email does not exist or the account is inactive: Withdraw not made");
    }
}
