package org.utopia.hackatonsesi_2025.scheduling.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.utopia.hackatonsesi_2025.patients.model.Patients;

@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointments_start", columnList = "start_time"),
        @Index(name = "idx_appointments_patient", columnList = "patient_id"),
        @Index(name = "idx_appointments_dentist_start", columnList = "dentist,start_time")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @Column(name = "dentist", length = 120, nullable = false)
    private String dentist; // nome do dentista responsável

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AppointmentStatus status;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "walk_in", nullable = false)
    @Builder.Default
    private boolean walkIn = false;
}
