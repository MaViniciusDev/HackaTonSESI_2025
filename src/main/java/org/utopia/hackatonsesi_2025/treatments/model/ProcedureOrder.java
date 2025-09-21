package org.utopia.hackatonsesi_2025.treatments.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.utopia.hackatonsesi_2025.patients.model.Patients;

@Entity
@Table(name = "procedure_orders", indexes = {
        @Index(name = "idx_proc_order_patient", columnList = "patient_id"),
        @Index(name = "idx_proc_order_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "procedure_id", nullable = false)
    private ProcedureCatalog procedure;

    @Column(name = "specialist_username", length = 120, nullable = false)
    private String specialistUsername;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProcedureOrderStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "scheduled_appointment_id")
    private Long scheduledAppointmentId;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}

