package org.utopia.hackatonsesi_2025.records.dto;

import java.time.LocalDateTime;

public record MedicalRecordResponseDTO(
        Long id,
        Long patientId,
        Long appointmentId,
        String anamnesis,
        String diagnosis,
        String procedures,
        String prescriptions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

