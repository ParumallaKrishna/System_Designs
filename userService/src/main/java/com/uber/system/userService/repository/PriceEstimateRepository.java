package com.uber.system.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uber.system.userService.dto.PriceEstimateResponse;

public interface PriceEstimateRepository extends JpaRepository<PriceEstimateResponse, Long> {
}