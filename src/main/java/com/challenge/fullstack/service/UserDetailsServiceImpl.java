package com.challenge.fullstack.service;

import com.challenge.fullstack.model.UserModel;
import com.challenge.fullstack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Llamada a la base de datos
        UserModel userModel = this.userRepository.findByName(username);

        if (userModel == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        System.out.println("Usuario encontrado: " + userModel);
        System.out.println("Contraseña en BD: " + userModel.getPassword());

        // Comprobación manual de la contraseña

        String testPassword = "password";
        boolean passwordMatches = new BCryptPasswordEncoder().matches("contraseña_prueba", userModel.getPassword());
        System.out.println("¿La contraseña coincide? " + passwordMatches);

        // Retornar UserDetails
        return User.builder()
                .username(userModel.getName())
                .password(userModel.getPassword())
                .roles(userModel.getRole().toUpperCase())
                .build();
    }

}
