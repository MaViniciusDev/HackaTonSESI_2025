package org.utopia.hackatonsesi_2025.records.dto;

public record MedicalRecordUpdateDTO(
        String anamnesis,
        String diagnosis,
        String procedures,
        String prescriptions
) {}

