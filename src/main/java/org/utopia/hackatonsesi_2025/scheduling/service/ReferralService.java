package org.utopia.hackatonsesi_2025.scheduling.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.patients.model.Patients;
import org.utopia.hackatonsesi_2025.patients.repository.PatientRepository;
import org.utopia.hackatonsesi_2025.scheduling.dto.AppointmentResponseDTO;
import org.utopia.hackatonsesi_2025.scheduling.dto.ReferralCreateDTO;
import org.utopia.hackatonsesi_2025.scheduling.dto.ReferralResponseDTO;
import org.utopia.hackatonsesi_2025.scheduling.dto.ReferralScheduleDTO;
import org.utopia.hackatonsesi_2025.scheduling.model.Appointment;
import org.utopia.hackatonsesi_2025.scheduling.model.AppointmentStatus;
import org.utopia.hackatonsesi_2025.scheduling.model.Referral;
import org.utopia.hackatonsesi_2025.scheduling.model.ReferralStatus;
import org.utopia.hackatonsesi_2025.scheduling.repository.AppointmentRepository;
import org.utopia.hackatonsesi_2025.scheduling.repository.ReferralRepository;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final PatientRepository patientRepository;
    private final ReferralRepository referralRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public ReferralResponseDTO createReferral(ReferralCreateDTO dto, String referringDentist) {
        Patients patient = patientRepository.findByCpf(dto.patientCpf())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo CPF"));
        Referral referral = Referral.builder()
                .patient(patient)
                .referringDentist(referringDentist)
                .specialistDentist(dto.specialistDentist())
                .status(ReferralStatus.PENDING)
                .reason(dto.reason())
                .createdAt(LocalDateTime.now())
                .build();
        referral = referralRepository.save(referral);
        return toDto(referral);
    }

    @Transactional(readOnly = true)
    public List<ReferralResponseDTO> listPendingByPatientCpf(String cpf) {
        return referralRepository.findByPatient_CpfAndStatus(cpf, ReferralStatus.PENDING)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponseDTO scheduleFromReferral(Long referralId, ReferralScheduleDTO dto) {
        Referral referral = referralRepository.findById(referralId)
                .orElseThrow(() -> new IllegalArgumentException("Encaminhamento não encontrado"));
        if (referral.getStatus() != ReferralStatus.PENDING) {
            throw new IllegalStateException("Encaminhamento não está pendente");
        }
        if (!dto.start().isBefore(dto.end())) {
            throw new IllegalArgumentException("Horário inicial deve ser antes do final");
        }
        // verificar conflito de agenda do especialista (ignora walk-ins)
        boolean overlap = appointmentRepository.existsByDentistAndWalkInFalseAndStartLessThanAndEndGreaterThan(
                referral.getSpecialistDentist(), dto.end(), dto.start());
        if (overlap) {
            throw new IllegalArgumentException("Já existe uma consulta nesse intervalo para o dentista");
        }
        Appointment appt = Appointment.builder()
                .patient(referral.getPatient())
                .dentist(referral.getSpecialistDentist())
                .start(dto.start())
                .end(dto.end())
                .status(AppointmentStatus.SCHEDULED)
                .notes("Encaminhado por: " + referral.getReferringDentist())
                .build();
        appt = appointmentRepository.save(appt);
        referral.setStatus(ReferralStatus.SCHEDULED);
        referral.setScheduledAppointmentId(appt.getId());
        referralRepository.save(referral);
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

    private ReferralResponseDTO toDto(Referral r) {
        return new ReferralResponseDTO(
                r.getId(),
                r.getPatient().getId(),
                r.getPatient().getName(),
                r.getPatient().getCpf(),
                r.getReferringDentist(),
                r.getSpecialistDentist(),
                r.getStatus(),
                r.getReason()
        );
    }
}
