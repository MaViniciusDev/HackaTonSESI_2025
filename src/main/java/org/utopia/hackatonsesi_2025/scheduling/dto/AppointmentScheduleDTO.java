package org.utopia.hackatonsesi_2025.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentScheduleDTO(
        @NotBlank String patientCpf,
        @NotBlank String dentist,
        @NotNull LocalDateTime start,
        @NotNull LocalDateTime end,
        String notes
) {}

