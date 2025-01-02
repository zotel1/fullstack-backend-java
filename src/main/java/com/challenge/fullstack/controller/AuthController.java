package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.AuthRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDto authRequestDto) {
        // gestionamos la autenticacionManager
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDto.getUser(), authRequestDto.getPassword()
        ));
        // Validamos el usuario en la base de datos
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequestDto.getUser());

    }
}
