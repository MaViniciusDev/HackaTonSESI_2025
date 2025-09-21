package org.utopia.hackatonsesi_2025.records.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.records.dto.MedicalRecordCreateDTO;
import org.utopia.hackatonsesi_2025.records.dto.MedicalRecordResponseDTO;
import org.utopia.hackatonsesi_2025.records.dto.MedicalRecordUpdateDTO;
import org.utopia.hackatonsesi_2025.records.service.MedicalRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DENTIST')")
public class MedicalRecordController {

    private final MedicalRecordService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponseDTO create(@Valid @RequestBody MedicalRecordCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public MedicalRecordResponseDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/by-patient/{patientId}")
    public List<MedicalRecordResponseDTO> listByPatient(@PathVariable Long patientId) {
        return service.listByPatient(patientId);
    }

    @PatchMapping("/{id}")
    public MedicalRecordResponseDTO update(@PathVariable Long id, @Valid @RequestBody MedicalRecordUpdateDTO dto) {
        return service.update(id, dto);
    }
}

