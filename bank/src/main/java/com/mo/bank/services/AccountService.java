package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.Card;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.repositories.CardRepository;
import com.mo.bank.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final CardRepository cardRepository;
    private final AuthService authService;

    public AccountService(AccountRepository accountRepository, JwtService jwtService, CardRepository cardRepository, AuthService authService) {
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.cardRepository = cardRepository;
        this.authService = authService;
    }

    public ResponseEntity<String> cardGenerate(String email, String token) {
        String emailFromToken = jwtService.extractEmail(token);

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        if (currentAccount.isEmpty()) {
            return ResponseEntity.badRequest().body("Account not found");
        }

        Account account = currentAccount.get();

        if (!isAccountActive(account)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your account is invalid (status = false)");
        }
        if (!isEmailValid(account, emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email from token does not match email from body");
        }
        if (account.getCreditCardNumber() != null) {
            return ResponseEntity.ok("You already have a card number");
        }

        String numbers = cardNumber();
        Card card = new Card(
                numbers,
                account.getName(),
                null,
                account.getAccountId()
        );
        cardRepository.save(card);

        account.setCreditCardNumber(numbers);
        accountRepository.save(account);

        return ResponseEntity.ok("You got a card number");
    }

    public ResponseEntity<String> passwordCardGenerate(Map<String, String> input , String token) {
        String email = input.get("email");
        String password = input.get("password");

        String emailFromToken = jwtService.extractEmail(token);

        Optional<Account> currentAccount = accountRepository.findByEmail(email);

        if (currentAccount.isEmpty()) {
            return ResponseEntity.badRequest().body("Account not found");
        }

        Account account = currentAccount.get();

        if (password.length() > 4) {
            return ResponseEntity.badRequest().body("Your password need 4 numbers");
        }
        if (!isAccountActive(account)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your account is invalid (status = false)");
        }
        if (!isEmailValid(account, emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email from token does not match email from body");
        }
        if (account.getCardPassword() != null) {
            return ResponseEntity.ok("You already have a card password");
        }

        Optional<Card> card = cardRepository.findByAccountId(account.getAccountId());

        if (card.isEmpty()) {
            return ResponseEntity.badRequest().body("Card not found");
        }
        if (card.get().getCardPassword() != null) {
            return ResponseEntity.badRequest().body("The card already has a password");
        }

        card.get().setCardPassword(password);
        cardRepository.save(card.get());

        account.setCardPassword(password);
        accountRepository.save(account);

        return ResponseEntity.ok("You got a card password");
    }

    public ResponseEntity<String> changeStatus(Map<String, String> input, String token) {
        String email = input.get("email");
        String status = input.get("status");

        String emailFromToken = jwtService.extractEmail(token);

        Optional<Account> currentAccount = accountRepository.findByEmail(email);

        if (currentAccount.isEmpty()) {
            return ResponseEntity.badRequest().body("Account not found");
        }

        Account account = currentAccount.get();

        if (!isEmailValid(account, emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email from token does not match email from body");
        }

        account.setStatus(Boolean.valueOf(status));
        accountRepository.save(account);

        if (account.getStatus().equals(false)) {
            return ResponseEntity.ok("Your account has been deactivated");
        }
        return ResponseEntity.ok("Your account has been activated");
    }

    public Account getAuth() {
        return authService.getAuthAccount();
    }

    private String cardNumber() {
        Random random = new Random();
        long cardNumber = Math.abs(1000000000000000L + random.nextLong(8999999999999999L));
        return String.valueOf(cardNumber);
    }

    public boolean isAccountActive(Account account) {
        return account.getStatus() == true;
    }

    public boolean isEmailValid(Account account, String emailFromToken) {
        return account.getEmail().equals(emailFromToken);
    }
}
