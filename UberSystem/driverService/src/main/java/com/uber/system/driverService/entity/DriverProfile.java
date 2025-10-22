package com.uber.system.driverService.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "driver_profiles")
public class DriverProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment in MySQL
    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(name = "vehicle_number", nullable = false, unique = true, length = 20)
    private String vehicleNumber;

    @Column(name = "vehicle_type", nullable = false, length = 30)
    private String vehicleType;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    public Long getDriverId() {
        return driverId;
    }

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

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean isAvailable) {
        this.available = isAvailable;
    }

    public java.time.LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(java.time.LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}
