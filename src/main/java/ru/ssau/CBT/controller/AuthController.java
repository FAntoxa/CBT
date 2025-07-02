package ru.ssau.CBT.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.CBT.model.LoginRequestDto;
import ru.ssau.CBT.model.LoginResponseDto;
import ru.ssau.CBT.model.User;
import ru.ssau.CBT.repository.UserRepository;
import ru.ssau.CBT.service.PasswordUtils;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User user = userRepository.findById(loginRequest.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(new LoginResponseDto(false, "User not found"));
        }
        
        if (passwordUtils.checkPassword(loginRequest.getPassword(), user.getPasswordhash())) {
            return ResponseEntity.ok(new LoginResponseDto(true, "Login successful"));
        } else {
            return ResponseEntity.status(401).body(new LoginResponseDto(false, "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody LoginRequestDto registerRequest) {
        if (userRepository.existsById(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new LoginResponseDto(false, "User already exists"));
        }
        
        String hashedPassword = passwordUtils.hashPassword(registerRequest.getPassword());
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPasswordhash(hashedPassword);
        
        userRepository.save(newUser);
        return ResponseEntity.ok(new LoginResponseDto(true, "User registered successfully"));
    }
} 