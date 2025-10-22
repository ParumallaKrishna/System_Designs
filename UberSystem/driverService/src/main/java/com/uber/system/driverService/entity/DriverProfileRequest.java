package com.uber.system.driverService.entity;

import lombok.Data;

@Data
public class DriverProfileRequest {
    private Long driverId;
    private String licenseNumber;
    private String vehicleNumber;
    private String vehicleType;

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getDriverId() {
        return driverId;
    }
}
