package org.utopia.hackatonsesi_2025.scheduling.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.scheduling.dto.AppointmentResponseDTO;
import org.utopia.hackatonsesi_2025.scheduling.dto.ReferralCreateDTO;
import org.utopia.hackatonsesi_2025.scheduling.dto.ReferralResponseDTO;
import org.utopia.hackatonsesi_2025.scheduling.dto.ReferralScheduleDTO;
import org.utopia.hackatonsesi_2025.scheduling.service.ReferralService;

@RestController
@RequestMapping("/api/referrals")
@RequiredArgsConstructor
public class ReferralController {

    private final ReferralService service;

    @PostMapping
    @PreAuthorize("hasRole('DENTIST')")
    public ReferralResponseDTO create(@Valid @RequestBody ReferralCreateDTO dto, Principal principal) {
        return service.createReferral(dto, principal.getName());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTION')")
    public List<ReferralResponseDTO> pendingByCpf(@RequestParam("cpf") @NotBlank String cpf) {
        return service.listPendingByPatientCpf(cpf);
    }

    @PostMapping("/{id}/schedule")
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTION')")
    public AppointmentResponseDTO schedule(@PathVariable Long id, @Valid @RequestBody ReferralScheduleDTO dto) {
        return service.scheduleFromReferral(id, dto);
    }
}

