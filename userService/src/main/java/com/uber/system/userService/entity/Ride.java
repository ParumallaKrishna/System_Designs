package com.uber.system.userService.entity;

import com.uber.system.userService.dto.DriverProfileDTO;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rides", indexes = {
        @Index(name = "idx_rider_id", columnList = "rider_id"),
        @Index(name = "idx_driver_id", columnList = "driver_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long riderId;
    private Double originLat;
    private Double originLng;
    private Double destLat;
    private Double destLng;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "driverId", column = @Column(name = "driver_id", nullable = true)),
            @AttributeOverride(name = "name", column = @Column(name = "driver_name", nullable = true)),
            @AttributeOverride(name = "rating", column = @Column(name = "driver_rating", nullable = true)),
            @AttributeOverride(name = "lat", column = @Column(name = "driver_lat", nullable = true)),
            @AttributeOverride(name = "lng", column = @Column(name = "driver_lng", nullable = true)),
            @AttributeOverride(name = "licenseNumber", column = @Column(name = "driver_license_number", nullable = true)),
            @AttributeOverride(name = "vehicleNumber", column = @Column(name = "driver_vehicle_number", nullable = true)),
            @AttributeOverride(name = "vehicleType", column = @Column(name = "driver_vehicle_type", nullable = true)),
            @AttributeOverride(name = "available", column = @Column(name = "driver_available", nullable = true))
    })
    private DriverProfileDTO driverProfileDTO;

    private String status; // CONFIRMED, NO_DRIVERS, COMPLETED
    private Double estimatedFare;
    private Integer etaMin;
}
