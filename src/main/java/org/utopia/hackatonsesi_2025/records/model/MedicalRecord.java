package org.utopia.hackatonsesi_2025.records.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.utopia.hackatonsesi_2025.patients.model.Patients;
import org.utopia.hackatonsesi_2025.scheduling.model.Appointment;

@Entity
@Table(name = "medical_records", indexes = {
        @Index(name = "idx_medical_records_patient", columnList = "patient_id"),
        @Index(name = "idx_medical_records_appointment", columnList = "appointment_id", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(name = "anamnesis", columnDefinition = "text")
    private String anamnesis;

    @Column(name = "diagnosis", columnDefinition = "text")
    private String diagnosis;

    @Column(name = "procedures", columnDefinition = "text")
    private String procedures;

    @Column(name = "prescriptions", columnDefinition = "text")
    private String prescriptions;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

