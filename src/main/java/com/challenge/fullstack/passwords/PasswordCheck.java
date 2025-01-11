package com.challenge.fullstack.passwords;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//public class PasswordCheck {
public class PasswordCheck {
    public static void main(String[] args) {
        String rawPassword = "admin";
        String storedHash = "$2a$10$EgbcqqE7Ph01T8hmcY73GuiB13gWUzMl/SSD4nQokeaZ66Rq/6E5K";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(rawPassword, storedHash);

        System.out.println("¿Contraseña válida? " + matches);
    }
}

//}