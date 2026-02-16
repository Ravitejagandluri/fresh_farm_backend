package com.freshfarm.service;

import com.freshfarm.config.JwtUtil;
import com.freshfarm.dto.AuthResponse;
import com.freshfarm.dto.LoginRequest;
import com.freshfarm.dto.ProfileResponse;
import com.freshfarm.dto.SignupRequest;
import com.freshfarm.dto.UpdateProfileRequest;
import com.freshfarm.entity.Role;
import com.freshfarm.entity.User;
import com.freshfarm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.BUYER);
        user.setPhone(request.getPhone());
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), token);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ProfileResponse getProfile(Long userId) {
        User user = findById(userId);
        ProfileResponse r = new ProfileResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setPhone(user.getPhone());
        r.setFarmName(user.getFarmName());
        r.setLocation(user.getLocation());
        r.setFarmingType(user.getFarmingType());
        r.setFarmSize(user.getFarmSize());
        r.setIrrigation(user.getIrrigation());
        r.setMainCrops(user.getMainCrops());
        r.setHarvestFrequency(user.getHarvestFrequency());
        r.setIsAvailable(user.getIsAvailable() != null ? user.getIsAvailable() : true);
        return r;
    }

    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findById(userId);
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getFarmName() != null) user.setFarmName(request.getFarmName());
        if (request.getLocation() != null) user.setLocation(request.getLocation());
        if (request.getFarmingType() != null) user.setFarmingType(request.getFarmingType());
        if (request.getFarmSize() != null) user.setFarmSize(request.getFarmSize());
        if (request.getIrrigation() != null) user.setIrrigation(request.getIrrigation());
        if (request.getMainCrops() != null) user.setMainCrops(request.getMainCrops());
        if (request.getHarvestFrequency() != null) user.setHarvestFrequency(request.getHarvestFrequency());
        if (request.getIsAvailable() != null) user.setIsAvailable(request.getIsAvailable());
        user = userRepository.save(user);
        return getProfile(user.getId());
    }
}
