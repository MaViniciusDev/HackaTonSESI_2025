package org.utopia.hackatonsesi_2025.treatments.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.treatments.dto.ProcedureCatalogResponseDTO;
import org.utopia.hackatonsesi_2025.treatments.dto.ProcedureScheduleRequestDTO;
import org.utopia.hackatonsesi_2025.treatments.model.Specialty;
import org.utopia.hackatonsesi_2025.treatments.service.TreatmentService;
import org.utopia.hackatonsesi_2025.scheduling.dto.AppointmentResponseDTO;

@RestController
@RequestMapping("/api/procedures")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService service;

    @GetMapping("/catalog")
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTION','DENTIST')")
    public List<ProcedureCatalogResponseDTO> catalog(@RequestParam(value = "specialty", required = false) Specialty specialty) {
        return service.listCatalog(specialty);
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTION')")
    public AppointmentResponseDTO schedule(@Valid @RequestBody ProcedureScheduleRequestDTO dto) {
        return service.schedule(dto);
    }
}

