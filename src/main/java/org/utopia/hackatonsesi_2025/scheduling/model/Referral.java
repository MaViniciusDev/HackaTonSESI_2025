package org.utopia.hackatonsesi_2025.scheduling.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.utopia.hackatonsesi_2025.patients.model.Patients;

@Entity
@Table(name = "referrals", indexes = {
        @Index(name = "idx_referrals_patient", columnList = "patient_id"),
        @Index(name = "idx_referrals_status", columnList = "status"),
        @Index(name = "idx_referrals_specialist", columnList = "specialist_dentist")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @Column(name = "referring_dentist", length = 120, nullable = false)
    private String referringDentist;

    @Column(name = "specialist_dentist", length = 120, nullable = false)
    private String specialistDentist;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ReferralStatus status;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "scheduled_appointment_id")
    private Long scheduledAppointmentId;
}

