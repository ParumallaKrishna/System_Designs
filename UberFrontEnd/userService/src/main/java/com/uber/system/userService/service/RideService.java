package com.uber.system.userService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.uber.system.common.DriverAcceptedEvent;
import com.uber.system.common.RideRequestedEvent;
import com.uber.system.userService.dto.DriverProfileDTO;
import com.uber.system.userService.dto.RideCreateRequest;
import com.uber.system.userService.dto.RideCreateResponse;
import com.uber.system.userService.dto.RideQuoteRequest;
import com.uber.system.userService.dto.RideQuoteResponse;
import com.uber.system.userService.entity.Ride;
import com.uber.system.userService.repository.RideRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepo;
    private final DriverNotificationService driverNotificationService;
    private final KafkaDriverNotificationService kafkaDriverNotificationService;

    public RideQuoteResponse getQuote(RideQuoteRequest req) {
        // Distance calculation (dummy: Euclidean distance * 111km approx per degree)
        double distanceKm = Math.sqrt(Math.pow(req.getDestLat() - req.getOriginLat(), 2)
                + Math.pow(req.getDestLng() - req.getOriginLng(), 2)) * 111;
        double durationMin = distanceKm / 0.5 * 1; // assuming 30 km/hr = 0.5 km/min
        double fare = distanceKm * 15; // ₹15/km

        List<DriverProfileDTO> available = driverNotificationService.notifyDriverProfileCreation();

        return RideQuoteResponse.builder()
                .distanceKm(distanceKm)
                .durationMin(durationMin)
                .estimatedFare(fare)
                .availableDrivers(available)
                .build();
    }

    public RideCreateResponse createRide(RideCreateRequest req) {
        Ride ride = Ride.builder()
                .riderId(req.getRiderId())
                .originLat(req.getOriginLat())
                .originLng(req.getOriginLng())
                .destLat(req.getDestLat())
                .destLng(req.getDestLng())
                .status("PENDING")
                .build();
        ride = rideRepo.save(ride);

        List<DriverProfileDTO> available = driverNotificationService.notifyDriverProfileCreation();
        System.out.println("Available drivers: " + available);
        if (available.isEmpty()) {
            ride.setStatus("NO_DRIVERS");
            rideRepo.save(ride);
            return RideCreateResponse.builder()
                    .rideId(ride.getId())
                    .status("NO_DRIVERS")
                    .build();
        }

        // Publish request to ALL drivers
        for (DriverProfileDTO driver : available) {
            kafkaDriverNotificationService.publishRideRequest(ride, driver);
        }

        // Wait for first acceptance
        Optional<DriverAcceptedEvent> acceptedOpt = kafkaDriverNotificationService.waitForFirstAcceptance(ride.getId(),
                30000);

        if (acceptedOpt.isPresent()) {
            DriverAcceptedEvent accepted = acceptedOpt.get();

            ride.setStatus("CONFIRMED");
            ride.setEstimatedFare(accepted.getFare());
            ride.setEtaMin(accepted.getEtaMin());

            DriverProfileDTO dto = new DriverProfileDTO();
            dto.setDriverId(accepted.getDriverId());
            dto.setVehicleNumber(accepted.getVehicleNumber());
            dto.setName(accepted.getDriverName());
            ride.setDriverProfileDTO(dto);

            rideRepo.save(ride);

            return RideCreateResponse.builder()
                    .rideId(ride.getId())
                    .driverProfileDTO(dto)
                    .status("CONFIRMED")
                    .etaMin(accepted.getEtaMin())
                    .estimatedFare(accepted.getFare())
                    .build();
        } else {
            ride.setStatus("NO_ACCEPTANCE");
            rideRepo.save(ride);
            return RideCreateResponse.builder()
                    .rideId(ride.getId())
                    .status("NO_ACCEPTANCE")
                    .build();
        }
    }
}