package org.utopia.hackatonsesi_2025.scheduling.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.utopia.hackatonsesi_2025.scheduling.dto.*;
import org.utopia.hackatonsesi_2025.scheduling.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    // Listagens filtradas por dentista logado
    @GetMapping("/today")
    @PreAuthorize("hasRole('DENTIST')")
    public List<AppointmentResponseDTO> today(Principal principal) {
        return service.getTodayAppointments(principal.getName());
    }

    @GetMapping("/past")
    @PreAuthorize("hasRole('DENTIST')")
    public List<AppointmentResponseDTO> pastByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal) {
        return service.getAppointmentsByDate(date, principal.getName());
    }

    @GetMapping("/future")
    @PreAuthorize("hasRole('DENTIST')")
    public List<AppointmentResponseDTO> futureByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Principal principal) {
        return service.getAppointmentsByDate(date, principal.getName());
    }

    @GetMapping("/search-by-cpf")
    @PreAuthorize("hasRole('DENTIST')")
    public List<AppointmentResponseDTO> byCpf(@RequestParam("cpf") @NotBlank String cpf, Principal principal) {
        return service.searchByPatientCpf(cpf, principal.getName());
    }

    @GetMapping("/search-by-name")
    @PreAuthorize("hasRole('DENTIST')")
    public List<AppointmentResponseDTO> byName(@RequestParam("name") @NotBlank String name, Principal principal) {
        return service.searchByPatientName(name, principal.getName());
    }

    // Disponibilidade para pacientes escolherem o dentista
    @GetMapping("/availability")
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTION','DENTIST')")
    public List<AvailabilitySlotDTO> availability(
            @RequestParam("dentist") String dentist,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "slotMinutes", defaultValue = "15") int slotMinutes) {
        return service.getAvailability(dentist, date, slotMinutes);
    }

    // Agendamento (paciente ou recepção)
    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT','RECEPTION','DENTIST')")
    public AppointmentResponseDTO schedule(@Valid @RequestBody AppointmentScheduleDTO dto) {
        return service.schedule(dto);
    }

    // Reagendamento (mesmo dentista)
    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('RECEPTION','DENTIST')")
    public AppointmentResponseDTO reschedule(@PathVariable Long id, @Valid @RequestBody AppointmentRescheduleDTO dto, Principal principal) {
        return service.reschedule(id, dto, principal.getName());
    }

    // Concluir consulta
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('DENTIST')")
    public AppointmentResponseDTO complete(@PathVariable Long id, Principal principal) {
        return service.complete(id, principal.getName());
    }

    // Walk-in (encaixe) — não pode conflitar com nenhum atendimento
    @PostMapping("/walk-in")
    @PreAuthorize("hasAnyRole('RECEPTION','DENTIST')")
    public AppointmentResponseDTO walkIn(@Valid @RequestBody AppointmentWalkInDTO dto) {
        return service.createWalkIn(dto);
    }

    // Reagendamento por paciente
    @PatchMapping("/{id}/reschedule-by-patient")
    @PreAuthorize("hasRole('PATIENT')")
    public AppointmentResponseDTO rescheduleByPatient(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRescheduleByPatientDTO dto) {
        return service.rescheduleByPatient(id, dto);
    }
}
