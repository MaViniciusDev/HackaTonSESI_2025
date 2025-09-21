package org.utopia.hackatonsesi_2025.patients.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PatientSelfRegisterDTO(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        LocalDate birthDate,
        @NotNull Sex sex,
        @Email @Size(max = 120) String email,
        @Size(max = 3) String ddd,
        @Size(max = 20) String phone1,
        @Size(max = 120) String address,
        @Size(max = 12) String addressNumber,
        @Size(max = 60) String neighborhood,
        @Size(max = 60) String city,
        @Size(max = 2) String state,
        @Size(max = 9) String cep
) {
    public enum Sex { MASCULINO, FEMININO, OUTRO }
}
