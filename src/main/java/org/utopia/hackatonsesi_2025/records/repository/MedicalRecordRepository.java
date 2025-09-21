package org.utopia.hackatonsesi_2025.records.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.records.model.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointment_Id(Long appointmentId);
    List<MedicalRecord> findByPatient_Id(Long patientId);
    boolean existsByAppointment_Id(Long appointmentId);
}

