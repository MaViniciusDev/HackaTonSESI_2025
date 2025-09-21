package org.utopia.hackatonsesi_2025.treatments.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.utopia.hackatonsesi_2025.treatments.dto.*;
import org.utopia.hackatonsesi_2025.treatments.model.*;
import org.utopia.hackatonsesi_2025.treatments.repository.*;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final ProcedureCatalogRepository catalogRepository;
    private final DentistProfileRepository dentistProfileRepository;
    private final ProcedureOrderRepository orderRepository;
    private final TreatmentAnalysisRepository analysisRepository;
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

    // ===== Evolução do tratamento =====

    @Transactional(readOnly = true)
    public List<ProcedureOrderResponseDTO> listOrdersByCpf(String cpf) {
        return orderRepository.findByPatient_CpfAndStatus(cpf, ProcedureOrderStatus.SCHEDULED)
                .stream().map(this::toOrderDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProcedureOrderResponseDTO getOrder(Long id) {
        ProcedureOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de procedimento não encontrada"));
        return toOrderDto(order);
    }

    @Transactional
    public ProcedureOrderResponseDTO updateOrderNotes(Long id, ProcedureOrderNotesUpdateDTO dto) {
        ProcedureOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de procedimento não encontrada"));
        order.setNotes(dto.notes());
        order = orderRepository.save(order);
        return toOrderDto(order);
    }

    @Transactional(readOnly = true)
    public List<TreatmentAnalysisResponseDTO> listAnalyses(Long orderId) {
        return analysisRepository.findByOrder_IdOrderByCreatedAtAsc(orderId).stream()
                .map(this::toAnalysisDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TreatmentAnalysisResponseDTO addAnalysis(Long orderId, TreatmentAnalysisCreateDTO dto) {
        ProcedureOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de procedimento não encontrada"));
        TreatmentAnalysis a = TreatmentAnalysis.builder()
                .order(order)
                .summary(dto.summary())
                .description(dto.description())
                .build();
        a = analysisRepository.save(a);
        return toAnalysisDto(a);
    }

    @Transactional
    public ProcedureOrderResponseDTO completeOrder(Long id) {
        ProcedureOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de procedimento não encontrada"));
        order.setStatus(ProcedureOrderStatus.COMPLETED);
        order = orderRepository.save(order);
        return toOrderDto(order);
    }

    // --- MÉTODOS PRIVADOS DE MAPEAMENTO ---

    private ProcedureCatalogResponseDTO toDto(ProcedureCatalog p) {
        return new ProcedureCatalogResponseDTO(
                p.getCode(), p.getName(), p.getRequiredSpecialty(), p.getMinDurationMinutes(), p.getMaxDurationMinutes(), p.isMultiSession()
        );
    }

    private ProcedureOrderResponseDTO toOrderDto(ProcedureOrder o) {
        return new ProcedureOrderResponseDTO(
                o.getId(),
                o.getPatient().getCpf(),
                o.getProcedure().getCode(),
                o.getProcedure().getName(),
                o.getProcedure().getRequiredSpecialty(),
                o.getSpecialistUsername(),
                o.getStatus(),
                o.getCreatedAt(),
                o.getScheduledAppointmentId(),
                o.getNotes()
        );
    }

    private TreatmentAnalysisResponseDTO toAnalysisDto(TreatmentAnalysis a) {
        return new TreatmentAnalysisResponseDTO(
                a.getId(),
                a.getSummary(),
                a.getDescription(),
                a.getCreatedAt()
        );
    }

    /**
     * Busca e formata os passos do progresso do tratamento para um paciente.
     * @param patientCpf O CPF do paciente logado.
     * @return Uma lista de PatientProgressDTO representando cada passo.
     */
    @Transactional(readOnly = true)
    public List<PatientProgressDTO> getPatientProgressSteps(String patientCpf) {
        List<ProcedureOrder> orders = orderRepository.findByPatient_CpfOrderByCreatedAtAsc(patientCpf);

        AtomicInteger stepCounter = new AtomicInteger(1);

        return orders.stream()
                .map(order -> {
                    String status;
                    // Lógica para determinar o status com base no seu enum ProcedureOrderStatus
                    if (order.getStatus() == ProcedureOrderStatus.COMPLETED) {
                        status = "CONCLUDED";
                    } else if (order.getStatus() == ProcedureOrderStatus.SCHEDULED) {
                        status = "SCHEDULED";
                    } else {
                        // Por padrão, se não estiver concluído ou agendado, consideramos pendente
                        status = "PENDING";
                    }

                    return new PatientProgressDTO(
                            stepCounter.getAndIncrement(),
                            order.getProcedure().getName(),
                            order.getProcedure().getDescription(),
                            status
                    );
                })
                .collect(Collectors.toList());
    }
}