package org.utopia.hackatonsesi_2025.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.patients.repository.PatientRepository;
import org.utopia.hackatonsesi_2025.users.dto.UserRegisterDTO;
import org.utopia.hackatonsesi_2025.users.dto.UserResponseDTO;
import org.utopia.hackatonsesi_2025.users.model.AppUser;
import org.utopia.hackatonsesi_2025.users.model.UserRole;
import org.utopia.hackatonsesi_2025.users.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO register(UserRegisterDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já possui usuário cadastrado");
        }
        AppUser.AppUserBuilder builder = AppUser.builder()
                .cpf(dto.cpf())
                .passwordHash(passwordEncoder.encode(dto.password()))
                .enabled(true);
        patientRepository.findByCpf(dto.cpf()).ifPresent(builder::patient);
        AppUser user = builder.build();
        user.getRoles().add(UserRole.PATIENT);
        user = repository.save(user);
        return new UserResponseDTO(user.getId(), user.getCpf(), user.isEnabled());
    }
}

