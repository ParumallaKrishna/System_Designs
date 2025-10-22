package com.uber.system.driverService.controller;

import com.uber.system.common.DriverAcceptedEvent;
import com.uber.system.driverService.DTO.AcceptRideRequest;
import com.uber.system.driverService.DTO.DriverProfileDTO;
import com.uber.system.driverService.entity.DriverProfile;
import com.uber.system.driverService.service.DriverProfileService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverRideController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DriverProfileService profileService;

    private static final String TOPIC_ACCEPT = "ride.accepted";

    @PostMapping("/accept")
    public ResponseEntity<?> acceptRide(@RequestBody AcceptRideRequest req) {
        DriverProfile profile = profileService.findById(req.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        DriverAcceptedEvent evt = new DriverAcceptedEvent();
        evt.setRideId(req.getRideId());
        evt.setDriverId(profile.getDriverId());
        evt.setDriverName(profile.getLicenseNumber());
        evt.setVehicleNumber(profile.getVehicleNumber());
        evt.setFare(req.getFare());
        evt.setEtaMin(req.getEtaMin());
        evt.setAcceptedAtMs(System.currentTimeMillis());

        kafkaTemplate.send(TOPIC_ACCEPT, String.valueOf(req.getRideId()), evt);

        // mark driver as busy
        profile.setAvailable(false);
        profileService.saveOrUpdateProfile(profile);

        return ResponseEntity.ok("ACCEPTED");
    }
}
