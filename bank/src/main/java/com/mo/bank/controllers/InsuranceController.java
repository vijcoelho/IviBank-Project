package com.mo.bank.controllers;

import com.mo.bank.services.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> pay(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return insuranceService.getAllInsurance(input, token);
    }
}
