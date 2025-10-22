package com.uber.system.driverService.service;

import com.uber.system.driverService.entity.DriverProfile;
import com.uber.system.driverService.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverProfileService {
    private final DriverProfileRepository driverProfileRepository;

    public DriverProfile saveOrUpdateProfile(DriverProfile profile) {
        return driverProfileRepository.save(profile);
    }

    public List<DriverProfile> getAvailableDrivers() {
        return driverProfileRepository.findByAvailableTrue();
    }

    public Optional<DriverProfile> findById(Long driverId) {
        return driverProfileRepository.findBydriverId(driverId);
        // throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}
