package org.utopia.hackatonsesi_2025.users.dto;

public record UserResponseDTO(
        Long id,
        String cpf,
        boolean enabled
) {}

