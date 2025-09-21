package org.utopia.hackatonsesi_2025.treatments.dto;

import jakarta.validation.constraints.Size;

public record ProcedureOrderNotesUpdateDTO(
        @Size(max = 5000) String notes
) {}

