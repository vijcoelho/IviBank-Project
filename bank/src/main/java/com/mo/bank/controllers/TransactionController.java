package com.mo.bank.controllers;

import com.mo.bank.entities.Transaction;
import com.mo.bank.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/make")
    public ResponseEntity<?> make(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return transactionService.makeTransaction(input, token);
    }

    @GetMapping("/get_all/{id}")
    public List<Transaction> getAllById(@PathVariable Integer id, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return transactionService.findAllAccountTransaction(id, token);
    }
}
