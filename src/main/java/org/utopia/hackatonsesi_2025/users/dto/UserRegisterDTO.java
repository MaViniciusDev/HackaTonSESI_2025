package org.utopia.hackatonsesi_2025.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        @NotBlank @Size(min = 6, max = 100) String password
) {}

