package com.uber.system.userService.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.uber.system.common.DriverAcceptedEvent;
import com.uber.system.common.RideRequestedEvent;
import com.uber.system.userService.dto.DriverProfileDTO;
import com.uber.system.userService.entity.Ride;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaDriverNotificationService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ConcurrentMap<Long, CompletableFuture<DriverAcceptedEvent>> pending = new ConcurrentHashMap<>();

    private static final String TOPIC_REQUESTS = "ride.requests";

    // Publish ride request to a target driver
    public void publishRideRequest(Ride ride, DriverProfileDTO targetDriver) {
        RideRequestedEvent evt = new RideRequestedEvent();
        evt.setRideId(ride.getId());
        evt.setRiderId(ride.getRiderId());
        evt.setOriginLat(ride.getOriginLat());
        evt.setOriginLng(ride.getOriginLng());
        evt.setDestLat(ride.getDestLat());
        evt.setDestLng(ride.getDestLng());
        evt.setTargetDriverId(targetDriver.getDriverId());
        evt.setRequestedAtMs(System.currentTimeMillis());

        // prepare future only once per ride
        pending.putIfAbsent(ride.getId(), new CompletableFuture<>());

        kafkaTemplate.send(TOPIC_REQUESTS, String.valueOf(targetDriver.getDriverId()), evt);
    }

    // Wait for first acceptance
    public Optional<DriverAcceptedEvent> waitForFirstAcceptance(Long rideId, long timeoutMs) {
        CompletableFuture<DriverAcceptedEvent> future = pending.get(rideId);
        if (future == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(future.get(timeoutMs, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            pending.remove(rideId);
            return Optional.empty();
        }
    }

    // Called by listener when an accepted event is received
    public void completeAcceptance(DriverAcceptedEvent e) {
        CompletableFuture<DriverAcceptedEvent> f = pending.remove(e.getRideId());
        if (f != null && !f.isDone()) {
            f.complete(e);
        }
    }
}
