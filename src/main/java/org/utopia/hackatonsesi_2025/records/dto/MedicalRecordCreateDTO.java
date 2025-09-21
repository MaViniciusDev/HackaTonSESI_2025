package org.utopia.hackatonsesi_2025.records.dto;

import jakarta.validation.constraints.NotNull;

public record MedicalRecordCreateDTO(
        @NotNull Long appointmentId,
        String anamnesis,
        String diagnosis,
        String procedures,
        String prescriptions
) {}

