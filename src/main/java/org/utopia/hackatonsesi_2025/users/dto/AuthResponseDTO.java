package org.utopia.hackatonsesi_2025.users.dto;

import java.util.List;

public record AuthResponseDTO(
        String token,
        long expiresIn,
        String tokenType,
        String cpf,
        List<String> roles
) {}

