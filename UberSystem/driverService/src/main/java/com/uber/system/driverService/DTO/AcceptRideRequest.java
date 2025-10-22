package com.uber.system.driverService.DTO;

import lombok.Data;

@Data
public class AcceptRideRequest {
    private Long rideId;
    private Long driverId;
    private Double fare;
    private Integer etaMin;
}
