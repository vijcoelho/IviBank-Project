package com.mo.bank.controllers;

import com.mo.bank.services.MobileInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/insurance")
public class MobileInsuranceController {

    private final MobileInsuranceService mobileInsuranceService;

    public MobileInsuranceController(MobileInsuranceService mobileInsuranceService) {
        this.mobileInsuranceService = mobileInsuranceService;
    }

    @PostMapping("/hire_mobile_insurance")
    public ResponseEntity<?> hire(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return mobileInsuranceService.hire(input, token);
    }

    @PutMapping("/pay_mobile_insurance")
    public ResponseEntity<?> pay(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return mobileInsuranceService.payInsurance(input, token);
    }

    @PutMapping("/deactivate_mobile_insurance")
    public ResponseEntity<?> deactivate(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return mobileInsuranceService.deactivate(input, token);
    }

    @PutMapping("/activate_mobile_insurance")
    public ResponseEntity<?> activate(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return mobileInsuranceService.activate(input, token);
    }
}
