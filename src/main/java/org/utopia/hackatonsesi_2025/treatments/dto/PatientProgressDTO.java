package org.utopia.hackatonsesi_2025.treatments.dto;

public record PatientProgressDTO(
        Integer step,
        String title,
        String description,
        String status
) {}