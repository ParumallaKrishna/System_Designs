package com.uber.system.driverService.service;

import com.uber.system.common.RideRequestedEvent;
import com.uber.system.driverService.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestListener {

    private final DriverProfileRepository driverRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "ride.requests", groupId = "driver-service")
    public void onRideRequested(RideRequestedEvent event) {
        // Push only to the targeted driver
        driverRepo.findById(event.getTargetDriverId()).ifPresent(driver -> {
            if (driver.getAvailable()) {
                String destination = "/topic/driver/" + driver.getDriverId();
                messagingTemplate.convertAndSend(destination, event);
            }
        });
    }
}
