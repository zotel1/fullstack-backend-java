package com.challenge.fullstack.repository;


import com.challenge.fullstack.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindByName() {
        // Crear un usuario de prueba
        UserModel user = new UserModel(null, "testuser", "password", "ROLE_USER");
        userRepository.save(user);

        // Buscar al usuario por nombre
        UserModel found = userRepository.findByUsername("testuser");

        // Validar que se encontró y los datos son correctos
        assertNotNull(found, "El usuario no debería ser nulo");
        assertEquals("testuser", found.getUsername(), "El nombre no coincide");
        assertEquals("password", found.getPassword(), "La contraseña no coincide");
        assertEquals("ROLE_USER", found.getRole(), "El rol no coincide");
    }
}
