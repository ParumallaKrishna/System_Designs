package com.uber.system.driverService.repository;

import com.uber.system.driverService.entity.DriverProfile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {
    List<DriverProfile> findByAvailableTrue();

    Optional<DriverProfile> findBydriverId(Long driverId);

}
