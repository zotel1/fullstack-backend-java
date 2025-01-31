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
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Llamada a la base de datos para obtener el usuario
        UserModel userModel = this.userRepository.findByUsername(username);

        if (userModel == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        System.out.println("Usuario encontrado: " + userModel.getUsername());
        System.out.println("Rol del usuario: " + userModel.getRole());

        // Normaliza el rol para asegurar que tiene el prefijo "ROLE_"
        String role = userModel.getRole().startsWith("ROLE_") ? userModel.getRole() : "ROLE_" + userModel.getRole();

        // Retorna un objeto UserDetails con los detalles del usuario
        return User.builder()
                .username(userModel.getUsername()) // Nombre de usuario
                .password(userModel.getPassword()) // Contraseña encriptada
                .roles(role.replaceFirst("^ROLE_", "")) // Normaliza roles redundantes
                .build();
    }
}


