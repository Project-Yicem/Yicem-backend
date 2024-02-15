package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.auth.JwtUtil;
import com.yicem.backend.yicem.dtos.request.LoginRequest;
import com.yicem.backend.yicem.dtos.response.LoginResponse;
import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(loginResponse);
    }
}
