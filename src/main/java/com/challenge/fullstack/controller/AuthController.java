package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.AuthRequestDto;
import com.challenge.fullstack.service.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDto authRequestDto) {

        try {
            // gestionamos la autenticacionManager
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequestDto.getUser(), authRequestDto.getPassword()
            ));
            // Validamos el usuario en la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequestDto.getUser());

            // e. Generamos el token
            String jwt = this.jwtUtilService.generateToken(userDetails);

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error Authentication" + e.getMessage());
        }


    }
}
