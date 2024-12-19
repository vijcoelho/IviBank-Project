package com.mo.bank.controllers;

import com.mo.bank.controllers.response.LoginResponse;
import com.mo.bank.entities.Account;
import com.mo.bank.security.JwtService;
import com.mo.bank.security.configs.SecurityConfiguration;
import com.mo.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    public AccountController(JwtService jwtService, AccountService accountService) {
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registeredAccount = accountService.signup(account);
        return ResponseEntity.ok(registeredAccount);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Map<String, String> input) {
        Account authAccount = accountService.login(input);
        if (authAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwtToken = jwtService.generateToken(authAccount);
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
