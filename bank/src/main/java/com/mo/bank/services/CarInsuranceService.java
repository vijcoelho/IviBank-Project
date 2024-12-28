package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.CarInsurance;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.repositories.CarInsuranceRepository;
import com.mo.bank.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class CarInsuranceService {

    private final AccountRepository accountRepository;
    private final CarInsuranceRepository carInsuranceRepository;
    private final JwtService jwtService;

    public CarInsuranceService(
            AccountRepository accountRepository,
            CarInsuranceRepository carInsuranceRepository,
            JwtService jwtService
    ) {
        this.accountRepository = accountRepository;
        this.carInsuranceRepository = carInsuranceRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> hire(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<CarInsurance> currentCarInsurance = carInsuranceRepository.findCarInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );
        if (!currentCarInsurance.isEmpty()) {
            return ResponseEntity.badRequest().body("You already have the car insurance");
        }

        CarInsurance carInsurance = CarInsurance.builder()
                .isActive(true)
                .isPaid(false)
                .price(475.00)
                .date(new Date())
                .accountId(currentAccount.get().getAccountId())
                .build();
        carInsuranceRepository.save(carInsurance);

        return ResponseEntity.ok(carInsurance);
    }

    public ResponseEntity<?> payInsurance(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<CarInsurance> currentCarInsurance = carInsuranceRepository.findCarInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentAccount.get().getMoney() < currentCarInsurance.get().getPrice()) {
            return ResponseEntity.badRequest().body("You don't have money to pay the car insurance");
        }

        Double newAmount = currentAccount.get().getMoney() - currentCarInsurance.get().getPrice();
        currentAccount.get().setMoney(newAmount);

        currentCarInsurance.get().setIsPaid(true);
        accountRepository.save(currentAccount.get());

        return ResponseEntity.ok("You pay your car insurance, enjoy");
    }

    public ResponseEntity<?> deactivate(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<CarInsurance> currentCarInsurance = carInsuranceRepository.findCarInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentCarInsurance.get().getIsActive().equals(false)) {
            return ResponseEntity.badRequest().body("Your car insurance is already deactivate");
        }

        currentCarInsurance.get().setIsActive(false);
        carInsuranceRepository.save(currentCarInsurance.get());

        return ResponseEntity.ok("Your car insurance is deactivate");
    }

    public ResponseEntity<?> activate(Map<String, String> input, String token) {
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<CarInsurance> currentCarInsurance = carInsuranceRepository.findCarInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        if (currentCarInsurance.get().getIsActive().equals(true)) {
            return ResponseEntity.badRequest().body("Your car insurance is already activate");
        }

        currentCarInsurance.get().setIsActive(true);
        carInsuranceRepository.save(currentCarInsurance.get());

        return ResponseEntity.ok("Your car insurance is activate");
    }
}
