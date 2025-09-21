package org.utopia.hackatonsesi_2025.users.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.users.model.AppUser;
import org.utopia.hackatonsesi_2025.users.model.UserRole;
import org.utopia.hackatonsesi_2025.users.repository.AppUserRepository;

@Configuration
@RequiredArgsConstructor
public class UsersDataInitializer {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    @PostConstruct
    @Transactional
    public void init() {
        seed("11111111111", "senha", UserRole.DENTIST);
        seed("22222222222", "senha", UserRole.RECEPTION);
        seed("12345678901", "senha12345", UserRole.PATIENT);
    }

    private void seed(String cpf, String rawPassword, UserRole role) {
        if (users.existsByCpf(cpf)) return;
        AppUser u = AppUser.builder()
                .cpf(cpf)
                .passwordHash(encoder.encode(rawPassword))
                .enabled(true)
                .build();
        u.getRoles().add(role);
        users.save(u);
    }
}

