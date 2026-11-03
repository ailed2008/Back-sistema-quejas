package com.banco.app_quejas.service;

import com.banco.app_quejas.dto.AuthResponse;
import com.banco.app_quejas.dto.LoginRequest;
import com.banco.app_quejas.dto.RegisterRequest;
import com.banco.app_quejas.entity.Rol;
import com.banco.app_quejas.entity.Usuario;
import com.banco.app_quejas.repository.UserRepository;
import com.banco.app_quejas.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol() != null ? request.rol() : Rol.EJECUTIVO)
                .build();

        userRepository.save(usuario);
        var jwtToken = jwtService.generarToken(usuario); // <-- Usando el método en español
        return new AuthResponse(jwtToken); // <-- Instanciación correcta para un record
    }

    public AuthResponse login(LoginRequest request) {
        // Valida las credenciales usando el AuthenticationManager configurado
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // Si es válido, busca el usuario y genera el token
        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var jwtToken = jwtService.generarToken(user); // <-- Usando el método en español
        return new AuthResponse(jwtToken); // <-- Instanciación correcta para un record
    }
}