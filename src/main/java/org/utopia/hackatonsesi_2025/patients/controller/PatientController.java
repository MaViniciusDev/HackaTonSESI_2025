package org.utopia.hackatonsesi_2025.patients.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.patients.dto.PatientIntakeDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientReceptionUpdateDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientResponseDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientSelfRegisterDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientSelfUpdateDTO;
import org.utopia.hackatonsesi_2025.patients.service.PatientService;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService service;

    @PostMapping("/self-register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PATIENT')")
    public PatientResponseDTO selfRegister(@Valid @RequestBody PatientSelfRegisterDTO dto) {
        return service.createFromSelfRegister(dto);
    }

    @PostMapping("/intake")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('RECEPTION')")
    public PatientResponseDTO intake(@Valid @RequestBody PatientIntakeDTO dto) {
        return service.createFromReception(dto);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('RECEPTION','DENTIST')")
    public List<PatientResponseDTO> searchByName(@RequestParam("name") String name) {
        return service.searchByName(name);
    }

    @GetMapping("/by-cpf/{cpf}")
    @PreAuthorize("hasAnyRole('RECEPTION','DENTIST')")
    public PatientResponseDTO getByCpf(@PathVariable String cpf) {
        return service.getByCpf(cpf);
    }

    @GetMapping("/by-rg/{rg}")
    @PreAuthorize("hasAnyRole('RECEPTION','DENTIST')")
    public PatientResponseDTO getByRg(@PathVariable String rg) {
        return service.getByRg(rg);
    }

    @PatchMapping("/self/{cpf}")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientResponseDTO selfUpdate(@PathVariable String cpf, @Valid @RequestBody PatientSelfUpdateDTO dto) {
        // Observação: em produção, vincule o usuário autenticado ao próprio paciente.
        return service.updateSelf(cpf, dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('RECEPTION')")
    public PatientResponseDTO updateFromReception(@PathVariable Long id, @Valid @RequestBody PatientReceptionUpdateDTO dto) {
        return service.updateFromReception(id, dto);
    }
}
