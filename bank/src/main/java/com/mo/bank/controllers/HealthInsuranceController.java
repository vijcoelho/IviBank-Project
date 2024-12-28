package com.mo.bank.controllers;

import com.mo.bank.services.HealthInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/insurance")
public class HealthInsuranceController {

    private final HealthInsuranceService healthInsuranceService;

    public HealthInsuranceController(HealthInsuranceService healthInsuranceService) {
        this.healthInsuranceService = healthInsuranceService;
    }

    @PostMapping("/hire_health_insurance")
    public ResponseEntity<?> hire(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return healthInsuranceService.hire(input, token);
    }

    @PutMapping("/pay_health_insurance")
    public ResponseEntity<?> pay(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return healthInsuranceService.payInsurance(input, token);
    }

    @PutMapping("/deactivate_health_insurance")
    public ResponseEntity<?> deactivate(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return healthInsuranceService.deactivate(input, token);
    }

    @PutMapping("/activate_health_insurance")
    public ResponseEntity<?> activate(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return healthInsuranceService.activate(input, token);
    }
}
