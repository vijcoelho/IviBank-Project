package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.HealthInsurance;
import com.mo.bank.entities.MobileInsurance;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.repositories.MobileInsuranceRepository;
import com.mo.bank.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class MobileInsuranceService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final MobileInsuranceRepository mobileInsuranceRepository;

    public MobileInsuranceService(
            AccountRepository accountRepository,
            JwtService jwtService,
            MobileInsuranceRepository mobileInsuranceRepository
    ) {
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.mobileInsuranceRepository = mobileInsuranceRepository;
    }

    public ResponseEntity<?> hire(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<MobileInsurance> currentMobileInsurance = mobileInsuranceRepository.findMobileInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );
        if (!currentMobileInsurance.isEmpty()) {
            return ResponseEntity.badRequest().body("You already have the mobile insurance");
        }

        MobileInsurance mobileInsurance = MobileInsurance.builder()
                .isActive(true)
                .isPaid(false)
                .price(200.00)
                .date(new Date())
                .accountId(currentAccount.get().getAccountId())
                .build();
        mobileInsuranceRepository.save(mobileInsurance);

        return ResponseEntity.ok(mobileInsurance);
    }

    public ResponseEntity<?> payInsurance(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<MobileInsurance> currentMobileInsurance = mobileInsuranceRepository.findMobileInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentAccount.get().getMoney() < currentMobileInsurance.get().getPrice()) {
            return ResponseEntity.badRequest().body("You don't have money to pay the mobile insurance");
        }

        Double newAmount = currentAccount.get().getMoney() - currentMobileInsurance.get().getPrice();
        currentAccount.get().setMoney(newAmount);

        currentMobileInsurance.get().setIsPaid(true);
        accountRepository.save(currentAccount.get());

        return ResponseEntity.ok("You pay your mobile insurance, enjoy");
    }

    public ResponseEntity<?> deactivate(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<MobileInsurance> currentMobileInsurance = mobileInsuranceRepository.findMobileInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentMobileInsurance.get().getIsActive().equals(false)) {
            return ResponseEntity.badRequest().body("Your mobile insurance is already deactivate");
        }

        currentMobileInsurance.get().setIsActive(false);
        mobileInsuranceRepository.save(currentMobileInsurance.get());

        return ResponseEntity.ok("Your mobile insurance is deactivate");
    }

    public ResponseEntity<?> activate(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<MobileInsurance> currentMobileInsurance = mobileInsuranceRepository.findMobileInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentMobileInsurance.get().getIsActive().equals(true)) {
            return ResponseEntity.badRequest().body("Your mobile insurance is already activate");
        }

        currentMobileInsurance.get().setIsActive(true);
        mobileInsuranceRepository.save(currentMobileInsurance.get());

        return ResponseEntity.ok("Your mobile insurance is activate");
    }
}
