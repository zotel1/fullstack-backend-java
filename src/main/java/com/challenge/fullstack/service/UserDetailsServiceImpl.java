package com.challenge.fullstack.service;

import com.challenge.fullstack.model.UserModel;
import com.challenge.fullstack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Llamada a la base de datos
        UserModel userModel = this.userRepository.findByName(username);

        if(userModel == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userModel.getName(), userModel.getPassword(), new ArrayList<>());
    }
}
