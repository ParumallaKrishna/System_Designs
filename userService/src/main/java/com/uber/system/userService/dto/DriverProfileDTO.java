package com.uber.system.userService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class DriverProfileDTO {
    @Column(name = "driver_id", nullable = true)
    private Long driverId; // frontend expects 'id'

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "rating", nullable = true)
    private Double rating; // use wrapper to allow null

    @Column(name = "lat", nullable = true)
    private Double lat; // location

    @Column(name = "lng", nullable = true)
    private Double lng; // location

    @Column(name = "license_number", nullable = true)
    private String licenseNumber;

    @Column(name = "vehicle_number", nullable = true)
    private String vehicleNumber;

    @Column(name = "vehicle_type", nullable = true)
    private String vehicleType;

    @Column(name = "available", nullable = true)
    private Boolean available; // wrapper to allow null

    public Long getDriverId() {
        return driverId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
