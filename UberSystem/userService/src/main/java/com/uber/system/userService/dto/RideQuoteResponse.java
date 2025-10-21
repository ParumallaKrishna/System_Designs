package com.uber.system.userService.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RideQuoteResponse {
    private double distanceKm;
    private double durationMin;
    private double estimatedFare;
    private List<DriverProfileDTO> availableDrivers;
}