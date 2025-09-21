package org.utopia.hackatonsesi_2025.scheduling.dto;

import java.time.LocalDateTime;
import org.utopia.hackatonsesi_2025.scheduling.model.AppointmentStatus;

public record AppointmentResponseDTO(
        Long id,
        Long patientId,
        String patientName,
        String patientCpf,
        String dentist,
        LocalDateTime start,
        LocalDateTime end,
        AppointmentStatus status,
        String notes
) {}

