package com.uber.system.userService.controller;

import com.uber.system.userService.entity.LoginRequest;
import com.uber.system.userService.entity.User;
import com.uber.system.userService.service.AuthService;
import com.uber.system.userService.dto.DriverRegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {
        try {
            String role = (String) request.get("role");
            if ("DRIVER".equalsIgnoreCase(role)) {
                DriverRegistrationRequest driverReq = new DriverRegistrationRequest();
                driverReq.setName((String) request.get("name"));
                driverReq.setEmail((String) request.get("email"));
                driverReq.setPhone((String) request.get("phone"));
                driverReq.setPassword((String) request.get("password"));
                driverReq.setRole(User.Role.DRIVER);
                driverReq.setLicenseNumber((String) request.get("licenseNumber"));
                driverReq.setVehicleNumber((String) request.get("vehicleNumber"));
                driverReq.setVehicleType((String) request.get("vehicleType"));
                User savedUser = authService.registerDriver(driverReq);
                return ResponseEntity.ok(savedUser);
            } else {
                User user = new User();
                user.setName((String) request.get("name"));
                user.setEmail((String) request.get("email"));
                user.setPhone((String) request.get("phone"));
                user.setPassword((String) request.get("password"));
                user.setRole(User.Role.RIDER);
                User savedUser = authService.register(user);
                return ResponseEntity.ok(savedUser);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration failed", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login request received: " + loginRequest);
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            if (email == null || password == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Email and password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            Map<String, Object> result = authService.login(email, password);

            if (result != null) {
                System.out.println(result);
                return ResponseEntity.ok(result); // returns {role}
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed");
        }
    }
}
