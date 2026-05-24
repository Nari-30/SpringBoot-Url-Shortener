package UrlShortener.controller;

import UrlShortener.model.RegisterRequest;
import UrlShortener.model.User;

import UrlShortener.repository.UserRepository;
import UrlShortener.model.LoginRequest;

import UrlShortener.security.JwtUtil;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody
            RegisterRequest request
    ){

        // Username Exists
        if(
            userRepository
                .findByUsername(
                    request.getUsername()
                )
                .isPresent()
        ){

            return ResponseEntity.badRequest()
                    .body(
                        "Username already exists"
                    );
        }

        // Email Exists
        if(
            userRepository
                .findByEmail(
                    request.getEmail()
                )
                .isPresent()
        ){

            return ResponseEntity.badRequest()
                    .body(
                        "Email already exists"
                    );
        }

        // Create User
        User user = new User();

        user.setUsername(
                request.getUsername()
        );

        user.setEmail(
                request.getEmail()
        );

        // Encrypt Password
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Save User
        userRepository.save(user);

        return ResponseEntity.ok(
                "User registered successfully"
        );
    }
    // Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody
            LoginRequest request
    ){

        // Find User
        User user =
                userRepository
                    .findByUsername(
                        request.getUsername()
                    )
                    .orElse(null);

        // Invalid Username
        if(user == null){

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Invalid username"
                    );
        }

        // Password Check
        if(
            !passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
            )
        ){

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Invalid password"
                    );
        }

        // Generate JWT Token
        String token =
                JwtUtil.generateToken(
                        user.getUsername()
                );

        return ResponseEntity.ok(token);
    }
}