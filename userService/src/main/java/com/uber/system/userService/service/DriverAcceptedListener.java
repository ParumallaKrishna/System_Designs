package com.uber.system.userService.service;

import com.uber.system.common.DriverAcceptedEvent;
import com.uber.system.userService.dto.DriverProfileDTO;
import com.uber.system.userService.entity.Ride;
import com.uber.system.userService.repository.RideRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverAcceptedListener {

    private final KafkaDriverNotificationService kafkaDriverNotificationService;
    private final RideRepository rideRepo;

    @KafkaListener(topics = "ride.accepted", groupId = "user-service")
    public void onDriverAccepted(DriverAcceptedEvent event) {
        // 1) complete pending future so createRide can continue
        kafkaDriverNotificationService.completeAcceptance(event);

        // 2) persist change in ride (set driver profile and change status -> CONFIRMED)
        Optional<Ride> opt = rideRepo.findById(event.getRideId());
        if (opt.isPresent()) {
            Ride ride = opt.get();
            // map accepted event into ride.driverProfileDTO or store minimal driver info
            // (assumes Ride has setDriverProfileDTO(...) or similar setter)
            DriverProfileDTO dto = new DriverProfileDTO();
            dto.setDriverId(event.getDriverId());
            dto.setVehicleNumber(event.getVehicleNumber());
            dto.setName(event.getDriverName());
            ride.setDriverProfileDTO(dto);
            ride.setStatus("CONFIRMED");
            ride.setEstimatedFare(event.getFare());
            if (event.getEtaMin() != null)
                ride.setEtaMin(event.getEtaMin());
            rideRepo.save(ride);
        }
    }
}
