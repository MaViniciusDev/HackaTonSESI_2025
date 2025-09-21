package org.utopia.hackatonsesi_2025.records.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.records.dto.MedicalRecordCreateDTO;
import org.utopia.hackatonsesi_2025.records.dto.MedicalRecordResponseDTO;
import org.utopia.hackatonsesi_2025.records.dto.MedicalRecordUpdateDTO;
import org.utopia.hackatonsesi_2025.records.model.MedicalRecord;
import org.utopia.hackatonsesi_2025.records.repository.MedicalRecordRepository;
import org.utopia.hackatonsesi_2025.scheduling.model.Appointment;
import org.utopia.hackatonsesi_2025.scheduling.repository.AppointmentRepository;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository repository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public MedicalRecordResponseDTO create(MedicalRecordCreateDTO dto) {
        if (repository.existsByAppointment_Id(dto.appointmentId())) {
            throw new IllegalStateException("Já existe prontuário para este atendimento");
        }
        Appointment appt = appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Atendimento não encontrado"));
        MedicalRecord record = MedicalRecord.builder()
                .appointment(appt)
                .patient(appt.getPatient())
                .anamnesis(dto.anamnesis())
                .diagnosis(dto.diagnosis())
                .procedures(dto.procedures())
                .prescriptions(dto.prescriptions())
                .build();
        record = repository.save(record);
        return toDTO(record);
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponseDTO get(Long id) {
        MedicalRecord record = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado"));
        return toDTO(record);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponseDTO> listByPatient(Long patientId) {
        return repository.findByPatient_Id(patientId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalRecordResponseDTO update(Long id, MedicalRecordUpdateDTO dto) {
        MedicalRecord record = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado"));
        if (dto.anamnesis() != null) record.setAnamnesis(dto.anamnesis());
        if (dto.diagnosis() != null) record.setDiagnosis(dto.diagnosis());
        if (dto.procedures() != null) record.setProcedures(dto.procedures());
        if (dto.prescriptions() != null) record.setPrescriptions(dto.prescriptions());
        record = repository.save(record);
        return toDTO(record);
    }

    private MedicalRecordResponseDTO toDTO(MedicalRecord record) {
        return new MedicalRecordResponseDTO(
                record.getId(),
                record.getPatient().getId(),
                record.getAppointment().getId(),
                record.getAnamnesis(),
                record.getDiagnosis(),
                record.getProcedures(),
                record.getPrescriptions(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}

