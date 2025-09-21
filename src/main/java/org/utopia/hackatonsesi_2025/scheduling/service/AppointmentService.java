package org.utopia.hackatonsesi_2025.scheduling.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.patients.model.Patients;
import org.utopia.hackatonsesi_2025.patients.repository.PatientRepository;
import org.utopia.hackatonsesi_2025.scheduling.dto.*;
import org.utopia.hackatonsesi_2025.scheduling.model.Appointment;
import org.utopia.hackatonsesi_2025.scheduling.model.AppointmentStatus;
import org.utopia.hackatonsesi_2025.scheduling.model.ReferralStatus;
import org.utopia.hackatonsesi_2025.scheduling.repository.AppointmentRepository;
import org.utopia.hackatonsesi_2025.scheduling.repository.ReferralRepository;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final PatientRepository patientRepository;
    private final ReferralRepository referralRepository;

    private static final LocalTime BUSINESS_START = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(18, 0);

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getTodayAppointments(String dentist) {
        LocalDate today = LocalDate.now();
        return getByDateRangeForDentist(toStart(today), toEnd(today), dentist);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByDate(LocalDate date, String dentist) {
        return getByDateRangeForDentist(toStart(date), toEnd(date), dentist);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> searchByPatientCpf(String cpf, String dentist) {
        return repository.findByDentistAndPatient_Cpf(dentist, cpf, Sort.by(Sort.Direction.DESC, "start"))
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> searchByPatientName(String name, String dentist) {
        return repository.findByDentistAndPatient_NameContainingIgnoreCase(dentist, name, Sort.by(Sort.Direction.DESC, "start"))
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AvailabilitySlotDTO> getAvailability(String dentist, LocalDate date, int slotMinutes) {
        LocalDateTime dayStart = date.atTime(BUSINESS_START);
        LocalDateTime dayEnd = date.atTime(BUSINESS_END);
        List<AvailabilitySlotDTO> free = new ArrayList<>();
        LocalDateTime cursor = dayStart;
        while (!cursor.plusMinutes(slotMinutes).isAfter(dayEnd)) {
            LocalDateTime slotEnd = cursor.plusMinutes(slotMinutes);
            boolean busy = repository.existsByDentistAndWalkInFalseAndStartLessThanAndEndGreaterThan(
                    dentist, slotEnd, cursor);
            if (!busy) {
                free.add(new AvailabilitySlotDTO(cursor, slotEnd));
            }
            cursor = cursor.plusMinutes(slotMinutes);
        }
        return free;
    }

    @Transactional
    public AppointmentResponseDTO schedule(AppointmentScheduleDTO dto) {
        if (!dto.start().isBefore(dto.end())) {
            throw new IllegalArgumentException("Horário inicial deve ser antes do final");
        }
        // Se houver encaminhamento pendente para este especialista, forçar uso do endpoint de encaminhamento
        boolean hasPendingReferralToThisDentist = referralRepository
                .findByPatient_CpfAndStatus(dto.patientCpf(), ReferralStatus.PENDING)
                .stream().anyMatch(r -> r.getSpecialistDentist().equals(dto.dentist()));
        if (hasPendingReferralToThisDentist) {
            throw new IllegalArgumentException("Há um encaminhamento pendente para este especialista. Agende usando o encaminhamento (POST /api/referrals/{id}/schedule)");
        }
        boolean conflict = repository.existsByDentistAndWalkInFalseAndStartLessThanAndEndGreaterThan(
                dto.dentist(), dto.end(), dto.start());
        if (conflict) {
            throw new IllegalArgumentException("Horário indisponível para o dentista");
        }
        Patients patient = patientRepository.findByCpf(dto.patientCpf())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo CPF"));
        Appointment appt = Appointment.builder()
                .patient(patient)
                .dentist(dto.dentist())
                .start(dto.start())
                .end(dto.end())
                .status(AppointmentStatus.SCHEDULED)
                .notes(dto.notes())
                .walkIn(false)
                .build();
        appt = repository.save(appt);
        return toDto(appt);
    }

    @Transactional
    public AppointmentResponseDTO reschedule(Long id, AppointmentRescheduleDTO dto, String dentist) {
        Appointment appt = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
        if (!appt.getDentist().equals(dentist)) {
            // manter mesmo dentista por padrão
            throw new IllegalArgumentException("Somente o mesmo dentista pode remarcar esta consulta");
        }
        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Consulta já finalizada");
        }
        if (!dto.start().isBefore(dto.end())) {
            throw new IllegalArgumentException("Horário inicial deve ser antes do final");
        }
        boolean conflict = repository.existsByDentistAndWalkInFalseAndIdNotAndStartLessThanAndEndGreaterThan(
                dentist, appt.getId(), dto.end(), dto.start());
        if (conflict) {
            throw new IllegalArgumentException("Horário indisponível para o dentista");
        }
        appt.setStart(dto.start());
        appt.setEnd(dto.end());
        appt = repository.save(appt);
        return toDto(appt);
    }

    @Transactional
    public AppointmentResponseDTO complete(Long id, String dentist) {
        Appointment appt = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
        if (!appt.getDentist().equals(dentist)) {
            throw new IllegalArgumentException("Somente o próprio dentista pode finalizar a consulta");
        }
        appt.setStatus(AppointmentStatus.COMPLETED);
        appt = repository.save(appt);
        return toDto(appt);
    }

    @Transactional
    public AppointmentResponseDTO createWalkIn(AppointmentWalkInDTO dto) {
        Patients patient = patientRepository.findByCpf(dto.patientCpf())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo CPF"));
        LocalDateTime start = dto.start() != null ? dto.start() : LocalDateTime.now();
        int minutes = dto.durationMinutes() != null ? dto.durationMinutes() : 15;
        LocalDateTime end = start.plusMinutes(minutes);
        // Walk-in NÃO pode conflitar com nenhum atendimento (marcado ou walk-in)
        boolean conflict = repository.existsByDentistAndStartLessThanAndEndGreaterThan(
                dto.dentist(), end, start);
        if (conflict) {
            throw new IllegalArgumentException("Já existe um atendimento nesse intervalo para o dentista");
        }
        Appointment appt = Appointment.builder()
                .patient(patient)
                .dentist(dto.dentist())
                .start(start)
                .end(end)
                .status(AppointmentStatus.SCHEDULED)
                .notes(dto.notes())
                .walkIn(true)
                .build();
        appt = repository.save(appt);
        return toDto(appt);
    }

    @Transactional
    public AppointmentResponseDTO rescheduleByPatient(Long id, AppointmentRescheduleByPatientDTO dto) {
        Appointment appt = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
        if (!appt.getPatient().getCpf().equals(dto.patientCpf())) {
            throw new IllegalArgumentException("Esta consulta não pertence a este paciente");
        }
        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Consulta já finalizada");
        }
        if (!dto.start().isBefore(dto.end())) {
            throw new IllegalArgumentException("Horário inicial deve ser antes do final");
        }
        boolean conflict = repository.existsByDentistAndIdNotAndStartLessThanAndEndGreaterThan(
                appt.getDentist(), appt.getId(), dto.end(), dto.start());
        if (conflict) {
            throw new IllegalArgumentException("Horário indisponível para o dentista");
        }
        appt.setStart(dto.start());
        appt.setEnd(dto.end());
        appt = repository.save(appt);
        return toDto(appt);
    }

    private List<AppointmentResponseDTO> getByDateRangeForDentist(LocalDateTime start, LocalDateTime end, String dentist) {
        return repository.findByDentistAndStartBetween(dentist, start, end, Sort.by(Sort.Direction.ASC, "start"))
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private LocalDateTime toStart(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime toEnd(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }

    private AppointmentResponseDTO toDto(Appointment a) {
        return new AppointmentResponseDTO(
                a.getId(),
                a.getPatient().getId(),
                a.getPatient().getName(),
                a.getPatient().getCpf(),
                a.getDentist(),
                a.getStart(),
                a.getEnd(),
                a.getStatus(),
                a.getNotes()
        );
    }
}
