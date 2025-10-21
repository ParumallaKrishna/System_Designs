// src/main/java/com/uber/system/userService/dto/RideCreateRequest.java
package com.uber.system.userService.dto;

import lombok.Data;

@Data
public class RideCreateRequest extends RideQuoteRequest {
    private Long riderId;
}