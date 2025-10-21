package com.uber.system.common;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class DriverAcceptedEvent {
    private Long rideId;
    private Long driverId;
    private String driverName;
    private String vehicleNumber;
    private Double fare;
    private Integer etaMin;
    private long acceptedAtMs;

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public void setEtaMin(Integer etaMin) {
        this.etaMin = etaMin;
    }

    public void setAcceptedAtMs(long acceptedAtMs) {
        this.acceptedAtMs = acceptedAtMs;
    }
}
