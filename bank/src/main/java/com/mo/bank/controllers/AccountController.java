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
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/generate_card")
    public ResponseEntity<String> generateCard(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return accountService.cardGenerate(input, token);
    }

    @PutMapping("/card_password")
    public ResponseEntity<String> cardPassword(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return accountService.passwordCardGenerate(input, token);
    }

    @PutMapping("/change_status")
    public ResponseEntity<String> changeStatus(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return accountService.changeStatus(input, token);
    }

    @GetMapping("/me")
    public Account getMe() {
        return accountService.getAuth();
    }
}
