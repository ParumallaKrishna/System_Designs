package com.uber.system.userService.service;

import com.uber.system.userService.dto.DriverProfileDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DriverNotificationService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String driverServiceUrl = "http://localhost:8080/driver";

    public void notifyDriverService(DriverProfileDTO profileDTO) {
        try {
            restTemplate.postForEntity(driverServiceUrl + "/profile", profileDTO, String.class);
        } catch (Exception e) {
            // Log or handle error
        }
    }

    public List<DriverProfileDTO> notifyDriverProfileCreation() {
        try {
            ResponseEntity<List<DriverProfileDTO>> response = restTemplate.exchange(
                    driverServiceUrl + "/available",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DriverProfileDTO>>() {
                    });
            System.out.println("Response from driver service: " + response);
            return response.getBody() != null ? response.getBody() : new ArrayList<>();

        } catch (Exception e) {
            // log error
            return new ArrayList<>();
        }
    }
}
