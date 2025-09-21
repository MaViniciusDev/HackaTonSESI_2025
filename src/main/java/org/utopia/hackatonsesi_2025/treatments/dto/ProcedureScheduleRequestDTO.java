package org.utopia.hackatonsesi_2025.treatments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ProcedureScheduleRequestDTO(
        @NotBlank String patientCpf,
        @NotBlank String procedureCode,
        @NotBlank String dentist,
        @NotNull LocalDateTime start,
        Integer durationMinutes,
        String notes
) {}

