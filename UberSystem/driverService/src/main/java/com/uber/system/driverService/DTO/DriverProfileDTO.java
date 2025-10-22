package com.uber.system.driverService.DTO;

import lombok.Data;

@Data
public class DriverProfileDTO {
    private Long driverId;
    private String licenseNumber;
    private String vehicleNumber;
    private String vehicleType;
    private boolean available;
    private String name; // optional
    private double rating; // optional
    private double lat; // optional
    private double lng; // optional
}
