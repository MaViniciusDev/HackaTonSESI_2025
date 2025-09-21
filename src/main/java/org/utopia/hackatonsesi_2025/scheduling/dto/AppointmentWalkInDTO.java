package org.utopia.hackatonsesi_2025.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record AppointmentWalkInDTO(
        @NotBlank String patientCpf,
        @NotBlank String dentist,
        LocalDateTime start,
        Integer durationMinutes,
        String notes
) {}
