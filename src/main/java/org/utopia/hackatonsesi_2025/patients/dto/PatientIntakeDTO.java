package org.utopia.hackatonsesi_2025.patients.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PatientIntakeDTO(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 30) String registration,
        @Size(max = 20) String status,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        LocalDate birthDate,
        @NotNull Sex sex,
        @Size(max = 20) String rg,
        @Size(max = 20) String rgIssuer,
        @Size(max = 120) String industrialCompany,
        @Size(max = 60) String convenio,
        @Size(max = 60) String unitAssignment,
        @Size(max = 3) String ddd,
        @Size(max = 20) String phone1,
        @Email @Size(max = 120) String email
) {
    public enum Sex { MASCULINO, FEMININO, OUTRO }
}
