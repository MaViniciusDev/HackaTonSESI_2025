package org.utopia.hackatonsesi_2025.treatments.dto;

import java.time.LocalDateTime;
import org.utopia.hackatonsesi_2025.treatments.model.ProcedureOrderStatus;
import org.utopia.hackatonsesi_2025.treatments.model.Specialty;

public record ProcedureOrderResponseDTO(
        Long id,
        String patientCpf,
        String procedureCode,
        String procedureName,
        Specialty procedureRequiredSpecialty,
        String specialistUsername,
        ProcedureOrderStatus status,
        LocalDateTime createdAt,
        Long scheduledAppointmentId,
        String notes
) {}

