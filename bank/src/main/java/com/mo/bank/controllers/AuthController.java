package com.mo.bank.controllers;

import com.mo.bank.controllers.response.LoginResponse;
import com.mo.bank.entities.Account;
import com.mo.bank.security.JwtService;
import com.mo.bank.security.configs.SecurityConfiguration;
import com.mo.bank.services.AccountService;
import com.mo.bank.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Account> register(@RequestBody Map<String, String> input) {
        Account registeredAccount = authService.signup(input);
        return ResponseEntity.ok(registeredAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> input) {
        ResponseEntity<?> response = authService.login(input);
        if (response.getStatusCode() == HttpStatus.OK) {
            Account authAccount = (Account) response.getBody();

            String jwtToken = jwtService.generateToken(authAccount);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);
        }
        return response;
    }

    @PutMapping("/change_password")
    public ResponseEntity<Account> changePassword(@RequestBody Map<String, String> input) {
        Account account = authService.changeEmailPassword(input);
        return ResponseEntity.ok(account);
    }
}
