package org.utopia.hackatonsesi_2025.scheduling.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRescheduleDTO(
        @NotNull LocalDateTime start,
        @NotNull LocalDateTime end
) {}

