package com.uber.system.userService.dto;

import lombok.Data;

@Data
public class RideQuoteRequest {
    private double originLat;
    private double originLng;
    private double destLat;
    private double destLng;
}