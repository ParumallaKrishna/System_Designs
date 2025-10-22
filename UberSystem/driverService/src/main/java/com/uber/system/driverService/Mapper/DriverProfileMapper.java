package com.uber.system.driverService.Mapper;

import com.uber.system.driverService.DTO.DriverProfileDTO;
import com.uber.system.driverService.entity.DriverProfile;

public class DriverProfileMapper {

    public static DriverProfileDTO toDTO(DriverProfile entity) {
        DriverProfileDTO dto = new DriverProfileDTO();
        dto.setDriverId(entity.getDriverId());
        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setVehicleNumber(entity.getVehicleNumber());
        dto.setVehicleType(entity.getVehicleType());
        dto.setAvailable(entity.getAvailable());
        // name, rating, lat/lng can be set from other tables/services if needed
        return dto;
    }
}