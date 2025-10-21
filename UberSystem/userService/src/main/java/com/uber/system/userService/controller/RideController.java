package com.uber.system.userService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uber.system.userService.dto.RideCreateRequest;
import com.uber.system.userService.dto.RideCreateResponse;
import com.uber.system.userService.dto.RideQuoteRequest;
import com.uber.system.userService.dto.RideQuoteResponse;
import com.uber.system.userService.service.RideService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/quote")
    public ResponseEntity<RideQuoteResponse> getQuote(@RequestBody RideQuoteRequest req) {
        return ResponseEntity.ok(rideService.getQuote(req));
    }

    @PostMapping
    public ResponseEntity<RideCreateResponse> createRide(@RequestBody RideCreateRequest req) {
        RideCreateResponse response = rideService.createRide(req);
        System.out.println("Received ride creation request: " + response);
        return ResponseEntity.ok(response);
    }
}