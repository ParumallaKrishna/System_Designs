package com.uber.system.common;

import lombok.Data;

@Data
public class RideRequestedEvent {
    private Long rideId;
    private Long riderId;
    private double originLat;
    private double originLng;
    private double destLat;
    private double destLng;
    private Long targetDriverId; // optional - the driver you're notifying
    private long requestedAtMs;

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLng() {
        return destLng;
    }

    public void setDestLng(double destLng) {
        this.destLng = destLng;
    }

    public Long getTargetDriverId() {
        return targetDriverId;
    }

    public void setTargetDriverId(Long targetDriverId) {
        this.targetDriverId = targetDriverId;
    }

    public long getRequestedAtMs() {
        return requestedAtMs;
    }

    public void setRequestedAtMs(long requestedAtMs) {
        this.requestedAtMs = requestedAtMs;
    }
}
