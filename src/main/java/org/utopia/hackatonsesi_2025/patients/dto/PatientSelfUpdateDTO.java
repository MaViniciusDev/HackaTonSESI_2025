package org.utopia.hackatonsesi_2025.patients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.utopia.hackatonsesi_2025.patients.model.Sex;

public record PatientSelfUpdateDTO(
        @Size(max = 120) String name,
        LocalDate birthDate,
        Sex sex,
        @Email @Size(max = 120) String email,
        @Size(max = 3) String ddd,
        @Size(max = 20) String phone1,
        @Size(max = 120) String address,
        @Size(max = 12) String addressNumber,
        @Size(max = 60) String neighborhood,
        @Size(max = 60) String city,
        @Pattern(regexp = "^[A-Z]{2}$") String state,
        @Size(max = 9) String cep
) {}
