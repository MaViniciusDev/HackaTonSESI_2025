package org.utopia.hackatonsesi_2025.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRescheduleByPatientDTO(
        @NotBlank String patientCpf,
        @NotNull LocalDateTime start,
        @NotNull LocalDateTime end
) {}

