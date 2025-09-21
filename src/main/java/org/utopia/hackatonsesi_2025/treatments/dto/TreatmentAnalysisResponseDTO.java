package org.utopia.hackatonsesi_2025.treatments.dto;

import java.time.LocalDateTime;

public record TreatmentAnalysisResponseDTO(
        Long id,
        String summary,
        String description,
        LocalDateTime createdAt
) {}

