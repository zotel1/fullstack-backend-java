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
@Tag(name = "Autenticacion", description = "Endpoints de autenticación")
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
        System.out.println("Contraseña ingresada: " + authRequestDto.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUser(), authRequestDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            System.out.println("Autenticación exitosa para el usuario: " + userDetails.getUsername());


            UserModel userModel = userRepository.findByName(authRequestDto.getUser());

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
        try {
            String username = jwtTokenService.extractUsername(refreshToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenService.validateToken(refreshToken, userDetails)) {
                UserModel user = userRepository.findByName(username);

                String newAccessToken = jwtTokenService.generateToken(userDetails, user.getRole());
                String newRefreshToken = jwtTokenService.generateRefreshToken(userDetails, user.getRole());

                AuthResponseDto response = new AuthResponseDto();
                response.setToken(newAccessToken);
                response.setRefreshToken(newRefreshToken);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de actualización inválido");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de actualización: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserModel userModel) {
        // Verificar si el usuario ya existe
        if (userRepository.findByName(userModel.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe");
        }

        // Encriptar la contraseña
      //  userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        System.out.println("Contraseña encriptada: " + userModel.getPassword());

        // Guardar el usuario
        userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }


}
