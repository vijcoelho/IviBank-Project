package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.CarInsurance;
import com.mo.bank.entities.HealthInsurance;
import com.mo.bank.entities.MobileInsurance;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.repositories.CarInsuranceRepository;
import com.mo.bank.repositories.HealthInsuranceRepository;
import com.mo.bank.repositories.MobileInsuranceRepository;
import com.mo.bank.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InsuranceService {

    private final CarInsuranceRepository carInsuranceRepository;
    private final HealthInsuranceRepository healthInsuranceRepository;
    private final MobileInsuranceRepository mobileInsuranceRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    public InsuranceService(
            CarInsuranceRepository carInsuranceRepository,
            HealthInsuranceRepository healthInsuranceRepository,
            MobileInsuranceRepository mobileInsuranceRepository,
            AccountRepository accountRepository,
            JwtService jwtService
    ) {
        this.carInsuranceRepository = carInsuranceRepository;
        this.healthInsuranceRepository = healthInsuranceRepository;
        this.mobileInsuranceRepository = mobileInsuranceRepository;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> getAllInsurance(Map<String, String> input, String token){
        String email = input.get("email");
        String emailFromToken = jwtService.extractEmail(token);

        if (!email.equals(emailFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email or token is wrong and different");
        }

        Optional<Account> currentAccount = accountRepository.findByEmail(email);
        Optional<CarInsurance> carInsurance = carInsuranceRepository.findCarInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );
        Optional<HealthInsurance> healthInsurance = healthInsuranceRepository.findHealthInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );
        Optional<MobileInsurance> mobileInsurance = mobileInsuranceRepository.findMobileInsuranceByAccountId(
                currentAccount.get().getAccountId()
        );

        Map<String, Object> insuranceData = new HashMap<>();
        insuranceData.put("carInsurance", carInsurance.orElse(null));
        insuranceData.put("healthInsurance", healthInsurance.orElse(null));
        insuranceData.put("mobileInsurance", mobileInsurance.orElse(null));

        return ResponseEntity.ok(insuranceData);

    }
}

