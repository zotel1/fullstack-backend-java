package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.AuthRequestDto;
import com.challenge.fullstack.dto.AuthResponseDto;
import com.challenge.fullstack.model.UserModel;
import com.challenge.fullstack.repository.UserRepository;
import com.challenge.fullstack.service.JwtTokenService;
import com.challenge.fullstack.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints de autenticación")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        System.out.println("Intentando autenticar al usuario: " + authRequestDto.getUser());
        try {
            // Autenticación
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUser(), authRequestDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Buscar al usuario
            UserModel userModel = userRepository.findByName(authRequestDto.getUser());
            if (userModel == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }

            // Generar tokens (ahora con dos argumentos)
            String accessToken = jwtTokenService.generateToken(userDetails, userModel.getRole());
            String refreshToken = jwtTokenService.generateRefreshToken(userDetails, userModel.getRole());

            AuthResponseDto response = new AuthResponseDto();
            response.setToken(accessToken);
            response.setRefreshToken(refreshToken);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error de autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token de actualización no puede estar vacío");
        }
        try {
            String username = jwtTokenService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Buscar el usuario para obtener el rol
            UserModel user = userRepository.findByName(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }

            if (jwtTokenService.validateToken(refreshToken, userDetails)) {
                String newAccessToken = jwtTokenService.generateToken(userDetails, user.getRole());
                String newRefreshToken = jwtTokenService.generateRefreshToken(userDetails, user.getRole());

                AuthResponseDto response = new AuthResponseDto();
                response.setToken(newAccessToken);
                response.setRefreshToken(newRefreshToken);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de actualización inválido o expirado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al procesar el token de actualización: " + e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserModel userModel) {
        // Verificar si el usuario ya existe
        if (userRepository.findByName(userModel.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe");
        }

        // Encriptar la contraseña
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        System.out.println("Contraseña encriptada: " + userModel.getPassword());

        // Guardar el usuario
        userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }
}
