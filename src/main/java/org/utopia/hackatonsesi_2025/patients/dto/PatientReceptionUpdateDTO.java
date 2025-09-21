package org.utopia.hackatonsesi_2025.patients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.utopia.hackatonsesi_2025.patients.model.Sex;

public record PatientReceptionUpdateDTO(
        @Size(max = 120) String name,
        @Size(max = 30) String registration,
        @Size(max = 20) String status,
        LocalDate birthDate,
        Sex sex,
        @Size(max = 20) String rg,
        @Size(max = 20) String rgIssuer,
        @Size(max = 120) String industrialCompany,
        @Size(max = 60) String convenio,
        @Size(max = 60) String unitAssignment,
        @Size(max = 3) String ddd,
        @Size(max = 20) String phone1,
        @Email @Size(max = 120) String email,
        @Size(max = 120) String address,
        @Size(max = 12) String addressNumber,
        @Size(max = 60) String neighborhood,
        @Size(max = 60) String city,
        @Size(max = 2) String state,
        @Size(max = 9) String cep,
        String notes
) {}
