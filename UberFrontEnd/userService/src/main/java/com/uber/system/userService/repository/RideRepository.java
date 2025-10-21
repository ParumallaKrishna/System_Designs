package com.uber.system.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uber.system.userService.entity.Ride;

public interface RideRepository extends JpaRepository<Ride, Long> {
}