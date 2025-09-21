package org.utopia.hackatonsesi_2025.treatments.dto;

import jakarta.validation.constraints.Size;

public record TreatmentAnalysisCreateDTO(
        @Size(max = 120) String summary,
        String description
) {}

