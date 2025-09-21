package org.utopia.hackatonsesi_2025.patients.dto;

import java.time.LocalDate;

public record PatientResponseDTO(
        Long id,
        String name,
        String cpf,
        LocalDate birthDate,
        String email
) {}
