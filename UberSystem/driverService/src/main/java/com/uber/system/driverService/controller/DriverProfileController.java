package com.uber.system.driverService.controller;

import com.uber.system.driverService.DTO.DriverProfileDTO;
import com.uber.system.driverService.Mapper.DriverProfileMapper;
import com.uber.system.driverService.entity.DriverProfile;
import com.uber.system.driverService.entity.DriverProfileRequest;
import com.uber.system.driverService.service.DriverProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverProfileController {
    private final DriverProfileService driverProfileService;

    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdateProfile(@RequestBody DriverProfileRequest request) {
        try {
            DriverProfile profile = new DriverProfile();
            profile.setLicenseNumber(request.getLicenseNumber());
            profile.setVehicleNumber(request.getVehicleNumber());
            profile.setVehicleType(request.getVehicleType());
            profile.setAvailable(true);
            profile.setLastActiveAt(java.time.LocalDateTime.now());
            DriverProfile savedProfile = driverProfileService.saveOrUpdateProfile(profile);
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create/update profile", e);
        }
    }

    @PostMapping("/init/{driverId}")
    public ResponseEntity<?> initDriverProfile(@PathVariable Long driverId) {
        try {
            DriverProfile profile = new DriverProfile();
            profile.setDriverId(driverId);
            profile.setLicenseNumber("");
            profile.setVehicleNumber("");
            profile.setVehicleType("");
            profile.setAvailable(false);
            profile.setLastActiveAt(java.time.LocalDateTime.now());
            driverProfileService.saveOrUpdateProfile(profile);
            return ResponseEntity.ok("Driver profile initialized");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to initialize driver profile", e);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverProfileDTO>> getAvailableDrivers() {
        try {
            System.out.println("Fetching available drivers in driverService...");
            List<DriverProfileDTO> availableDrivers = driverProfileService.getAvailableDrivers()
                    .stream()
                    .map(DriverProfileMapper::toDTO)
                    .collect(Collectors.toList());

            System.out.println("Available Drivers: " + availableDrivers);
            return ResponseEntity.ok(availableDrivers);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch available drivers", e);
        }
    }
}
