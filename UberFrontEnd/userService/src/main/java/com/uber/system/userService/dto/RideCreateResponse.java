// src/main/java/com/uber/system/userService/dto/RideCreateResponse.java
package com.uber.system.userService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideCreateResponse {
    private Long rideId;
    private DriverProfileDTO driverProfileDTO;
    private String status; // CONFIRMED, NO_DRIVERS
    private Integer etaMin;
    private Double estimatedFare;
}