package com.collabspace.backend.service;

import com.collabspace.backend.dto.request.LoginRequest;
import com.collabspace.backend.dto.request.RegisterRequest;
import com.collabspace.backend.dto.response.AuthResponse;
import com.collabspace.backend.dto.response.UserResponse;
import com.collabspace.backend.entity.User;
import com.collabspace.backend.exception.BadRequestException;
import com.collabspace.backend.exception.ConflictException;
import com.collabspace.backend.repository.UserRepository;
import com.collabspace.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail().trim().toLowerCase())){
            throw new ConflictException("User already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        String accessToken = jwtService.generateToken(userDetails);
        return new AuthResponse(accessToken, new UserResponse(user.getId(), user.getName(), user.getEmail()));
    }

    public UserResponse getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
