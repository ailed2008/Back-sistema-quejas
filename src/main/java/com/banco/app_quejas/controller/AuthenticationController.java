package com.banco.app_quejas.controller;

import com.banco.app_quejas.dto.AuthResponse;
import com.banco.app_quejas.dto.LoginRequest;
import com.banco.app_quejas.dto.RegisterRequest; // <-- Importa tu record de registro
import com.banco.app_quejas.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){ // <-- Usa RegisterRequest
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}