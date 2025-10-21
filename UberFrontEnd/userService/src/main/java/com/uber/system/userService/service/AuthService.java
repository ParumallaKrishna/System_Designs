package com.uber.system.userService.service;

import com.uber.system.userService.dto.DriverRegistrationRequest;
import com.uber.system.userService.entity.User;
import com.uber.system.userService.repository.UserRepository;
import com.uber.system.userService.dto.DriverProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DriverNotificationService driverNotificationService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public Map<String, Object> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();

            Map<String, Object> response = new HashMap<>();
            // response.put("token", token);
            response.put("role", user.getRole().name()); // 👈 include role
            if (user.getRole() == User.Role.RIDER) {
                // Option A: use DB ID (better, consistent across logins)
                response.put("riderId", user.getId());

                // Option B: if you REALLY want a random ID (demo only)
                // int randomRiderId = new Random().nextInt(9000) + 1000;
                // response.put("riderId", randomRiderId);
            } else if (user.getRole() == User.Role.DRIVER) {
                response.put("driverId", user.getId());
            }
            return response;
        }
        return null;
    }

    public User registerDriver(DriverRegistrationRequest driverReq) {
        User user = new User();
        user.setName(driverReq.getName());
        user.setEmail(driverReq.getEmail());
        user.setPhone(driverReq.getPhone());
        user.setPassword(passwordEncoder.encode(driverReq.getPassword()));
        user.setRole(User.Role.DRIVER);
        User savedUser = userRepository.save(user);
        DriverProfileDTO profileDTO = new DriverProfileDTO();
        profileDTO.setDriverId(savedUser.getId());
        profileDTO.setLicenseNumber(driverReq.getLicenseNumber());
        profileDTO.setVehicleNumber(driverReq.getVehicleNumber());
        profileDTO.setVehicleType(driverReq.getVehicleType());
        driverNotificationService.notifyDriverService(profileDTO);
        return savedUser;
    }
}
