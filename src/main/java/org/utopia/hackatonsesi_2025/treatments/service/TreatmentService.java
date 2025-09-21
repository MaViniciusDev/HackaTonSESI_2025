package org.utopia.hackatonsesi_2025.treatments.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.patients.model.Patients;
import org.utopia.hackatonsesi_2025.patients.repository.PatientRepository;
import org.utopia.hackatonsesi_2025.scheduling.dto.AppointmentResponseDTO;
import org.utopia.hackatonsesi_2025.scheduling.model.Appointment;
import org.utopia.hackatonsesi_2025.scheduling.model.AppointmentStatus;
import org.utopia.hackatonsesi_2025.scheduling.repository.AppointmentRepository;
import org.utopia.hackatonsesi_2025.treatments.dto.ProcedureCatalogResponseDTO;
import org.utopia.hackatonsesi_2025.treatments.dto.ProcedureScheduleRequestDTO;
import org.utopia.hackatonsesi_2025.treatments.model.*;
import org.utopia.hackatonsesi_2025.treatments.repository.*;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final ProcedureCatalogRepository catalogRepository;
    private final DentistProfileRepository dentistProfileRepository;
    private final ProcedureOrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<ProcedureCatalogResponseDTO> listCatalog(Specialty filter) {
        List<ProcedureCatalog> list = filter == null
                ? catalogRepository.findAll()
                : catalogRepository.findByRequiredSpecialty(filter);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponseDTO schedule(ProcedureScheduleRequestDTO dto) {
        ProcedureCatalog proc = catalogRepository.findByCode(dto.procedureCode())
                .orElseThrow(() -> new IllegalArgumentException("Procedimento não encontrado"));
        DentistProfile prof = dentistProfileRepository.findByDentistUsername(dto.dentist())
                .orElseThrow(() -> new IllegalArgumentException("Dentista não possui perfil cadastrado"));
        if (!prof.getSpecialties().contains(proc.getRequiredSpecialty())) {
            throw new IllegalArgumentException("Dentista não atende a especialidade mínima do procedimento");
        }
        Patients patient = patientRepository.findByCpf(dto.patientCpf())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo CPF"));
        // Verificar se o paciente já teve consulta COMPLETED com algum dentista dessa especialidade
        boolean hasCompletedWithSpecialty = appointmentRepository
                .findByPatient_Cpf(dto.patientCpf(), Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .anyMatch(a -> dentistProfileRepository.findByDentistUsername(a.getDentist())
                        .map(dp -> dp.getSpecialties().contains(proc.getRequiredSpecialty()))
                        .orElse(false));
        if (!hasCompletedWithSpecialty) {
            throw new IllegalStateException("O paciente precisa ter consulta concluída com o especialista antes de marcar o procedimento");
        }
        LocalDateTime start = dto.start();
        int duration = dto.durationMinutes() != null ? dto.durationMinutes() : proc.getMinDurationMinutes();
        LocalDateTime end = start.plusMinutes(duration);
        boolean conflict = appointmentRepository.existsByDentistAndStartLessThanAndEndGreaterThan(
                dto.dentist(), end, start);
        if (conflict) {
            throw new IllegalArgumentException("Horário indisponível para o dentista");
        }
        Appointment appt = Appointment.builder()
                .patient(patient)
                .dentist(dto.dentist())
                .start(start)
                .end(end)
                .status(AppointmentStatus.SCHEDULED)
                .notes("Procedimento: " + proc.getName() + (dto.notes() != null ? ". " + dto.notes() : ""))
                .walkIn(false)
                .build();
        appt = appointmentRepository.save(appt);
        ProcedureOrder order = ProcedureOrder.builder()
                .patient(patient)
                .procedure(proc)
                .specialistUsername(dto.dentist())
                .status(ProcedureOrderStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .scheduledAppointmentId(appt.getId())
                .notes(dto.notes())
                .build();
        orderRepository.save(order);
        return new AppointmentResponseDTO(
                appt.getId(),
                appt.getPatient().getId(),
                appt.getPatient().getName(),
                appt.getPatient().getCpf(),
                appt.getDentist(),
                appt.getStart(),
                appt.getEnd(),
                appt.getStatus(),
                appt.getNotes()
        );
    }

    private ProcedureCatalogResponseDTO toDto(ProcedureCatalog p) {
        return new ProcedureCatalogResponseDTO(
                p.getCode(), p.getName(), p.getRequiredSpecialty(), p.getMinDurationMinutes(), p.getMaxDurationMinutes(), p.isMultiSession()
        );
    }
}

