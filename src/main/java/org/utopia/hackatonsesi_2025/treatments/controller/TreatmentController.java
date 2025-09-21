package org.utopia.hackatonsesi_2025.treatments.controller;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.treatments.dto.*;
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

    // ===== Evolução do tratamento =====

    @GetMapping("/orders")
    @PreAuthorize("hasRole('DENTIST')")
    public List<ProcedureOrderResponseDTO> listOrdersByCpf(@RequestParam("cpf") String cpf) {
        return service.listOrdersByCpf(cpf);
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('DENTIST')")
    public ProcedureOrderResponseDTO getOrder(@PathVariable Long id) {
        return service.getOrder(id);
    }

    @PatchMapping("/orders/{id}/notes")
    @PreAuthorize("hasRole('DENTIST')")
    public ProcedureOrderResponseDTO updateNotes(@PathVariable Long id, @Valid @RequestBody ProcedureOrderNotesUpdateDTO dto) {
        return service.updateOrderNotes(id, dto);
    }

    @GetMapping("/orders/{id}/analyses")
    @PreAuthorize("hasRole('DENTIST')")
    public List<TreatmentAnalysisResponseDTO> listAnalyses(@PathVariable Long id) {
        return service.listAnalyses(id);
    }

    @PostMapping("/orders/{id}/analyses")
    @PreAuthorize("hasRole('DENTIST')")
    public TreatmentAnalysisResponseDTO addAnalysis(@PathVariable Long id, @Valid @RequestBody TreatmentAnalysisCreateDTO dto) {
        return service.addAnalysis(id, dto);
    }

    @PostMapping("/orders/{id}/complete")
    @PreAuthorize("hasRole('DENTIST')")
    public ProcedureOrderResponseDTO completeOrder(@PathVariable Long id) {
        return service.completeOrder(id);
    }

    /**
     * Retorna o progresso do tratamento formatado para o paciente logado.
     * @param principal Objeto do Spring Security que contém os dados do usuário logado.
     * @return Lista de passos do progresso.
     */
    @GetMapping("/patient-progress")
    @PreAuthorize("hasRole('PATIENT')")
    public List<PatientProgressDTO> getPatientProgress(Principal principal) {
        // O principal.getName() retorna o 'username', que no seu caso é o CPF do paciente.
        return service.getPatientProgressSteps(principal.getName());
    }
}

