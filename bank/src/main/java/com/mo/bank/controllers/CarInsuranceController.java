package com.mo.bank.controllers;

import com.mo.bank.services.CarInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/insurance")
public class CarInsuranceController {

    private final CarInsuranceService carInsuranceService;

    public CarInsuranceController(CarInsuranceService carInsuranceService) {
        this.carInsuranceService = carInsuranceService;
    }

    @PostMapping("/hire_car_insurance")
    public ResponseEntity<?> hire(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return carInsuranceService.hire(input, token);
    }

    @PutMapping("/pay_car_insurance")
    public ResponseEntity<?> pay(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return carInsuranceService.payInsurance(input, token);
    }

    @PutMapping("/deactivate_car_insurance")
    public ResponseEntity<?> deactivate(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return carInsuranceService.deactivate(input, token);
    }

    @PutMapping("/activate_car_insurance")
    public ResponseEntity<?> activate(@RequestBody Map<String, String> input, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        return carInsuranceService.activate(input, token);
    }
}
