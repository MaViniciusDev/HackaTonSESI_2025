package org.utopia.hackatonsesi_2025.scheduling.dto;

import jakarta.validation.constraints.NotBlank;

public record ReferralCreateDTO(
        @NotBlank String patientCpf,
        @NotBlank String specialistDentist,
        String reason
) {}

