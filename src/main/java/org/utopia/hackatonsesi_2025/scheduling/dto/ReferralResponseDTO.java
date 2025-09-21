package org.utopia.hackatonsesi_2025.scheduling.dto;

import org.utopia.hackatonsesi_2025.scheduling.model.ReferralStatus;

public record ReferralResponseDTO(
        Long id,
        Long patientId,
        String patientName,
        String patientCpf,
        String referringDentist,
        String specialistDentist,
        ReferralStatus status,
        String reason
) {}

