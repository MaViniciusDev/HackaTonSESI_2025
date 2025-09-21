package org.utopia.hackatonsesi_2025.treatments.dto;

import org.utopia.hackatonsesi_2025.treatments.model.Specialty;

public record ProcedureCatalogResponseDTO(
        String code,
        String name,
        Specialty requiredSpecialty,
        int minDurationMinutes,
        int maxDurationMinutes,
        boolean multiSession
) {}

