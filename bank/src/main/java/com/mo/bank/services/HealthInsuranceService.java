package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.CarInsurance;
import com.mo.bank.entities.HealthInsurance;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.repositories.HealthInsuranceRepository;
import com.mo.bank.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class HealthInsuranceService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final HealthInsuranceRepository healthInsuranceRepository;

    public HealthInsuranceService(
            AccountRepository accountRepository,
            JwtService jwtService,
            HealthInsuranceRepository healthInsuranceRepository
    ) {
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.healthInsuranceRepository = healthInsuranceRepository;
    }

    public ResponseEntity<?> hire(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<HealthInsurance> currentHealthInsurance = healthInsuranceRepository.findHealthInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );
        if (!currentHealthInsurance.isEmpty()) {
            return ResponseEntity.badRequest().body("You already have the health insurance");
        }

        HealthInsurance healthInsurance = HealthInsurance.builder()
                .isActive(true)
                .isPaid(false)
                .price(775.00)
                .date(new Date())
                .accountId(currentAccount.get().getAccountId())
                .build();
        healthInsuranceRepository.save(healthInsurance);

        return ResponseEntity.ok(healthInsurance);
    }

    public ResponseEntity<?> payInsurance(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<HealthInsurance> currentHealthInsurance = healthInsuranceRepository.findHealthInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentAccount.get().getMoney() < currentHealthInsurance.get().getPrice()) {
            return ResponseEntity.badRequest().body("You don't have money to pay the health insurance");
        }

        Double newAmount = currentAccount.get().getMoney() - currentHealthInsurance.get().getPrice();
        currentAccount.get().setMoney(newAmount);

        currentHealthInsurance.get().setIsPaid(true);
        accountRepository.save(currentAccount.get());

        return ResponseEntity.ok("You pay your health insurance, enjoy");
    }

    public ResponseEntity<?> deactivate(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<HealthInsurance> currentHealthInsurance = healthInsuranceRepository.findHealthInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentHealthInsurance.get().getIsActive().equals(false)) {
            return ResponseEntity.badRequest().body("Your health insurance is already deactivate");
        }

        currentHealthInsurance.get().setIsActive(false);
        healthInsuranceRepository.save(currentHealthInsurance.get());

        return ResponseEntity.ok("Your health insurance is deactivate");
    }

    public ResponseEntity<?> activate(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<HealthInsurance> currentHealthInsurance = healthInsuranceRepository.findHealthInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentHealthInsurance.get().getIsActive().equals(true)) {
            return ResponseEntity.badRequest().body("Your health insurance is already activate");
        }

        currentHealthInsurance.get().setIsActive(true);
        healthInsuranceRepository.save(currentHealthInsurance.get());

        return ResponseEntity.ok("Your health insurance is activate");
    }
}
