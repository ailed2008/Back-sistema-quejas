package com.banco.app_quejas.security;

import com.banco.app_quejas.entity.Rol;
import com.banco.app_quejas.entity.Usuario;
import com.banco.app_quejas.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args ->{
            //Crear usuaruario ADMIN por defecto
            if(userRepository.findByUsername("admin@banco.com").isEmpty()){
                Usuario admin = Usuario.builder()
                        .username("admin@banco.com")
                        .password(passwordEncoder.encode("Admin123*"))
                        .rol(Rol.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("Usuario ADMIN por defecto creado");
            }

            // Crear usuario EJECUTIVO por defecto si no existe
            if (userRepository.findByUsername("ejecutivo@banco.com").isEmpty()) {
                Usuario ejecutivo = Usuario.builder()
                        .username("ejecutivo@banco.com")
                        .password(passwordEncoder.encode("Ejecutivo123*"))
                        .rol(Rol.EJECUTIVO)
                        .build();
                userRepository.save(ejecutivo);
                System.out.println("✅ Usuario EJECUTIVO por defecto creado");
            }

        };
    }
}

