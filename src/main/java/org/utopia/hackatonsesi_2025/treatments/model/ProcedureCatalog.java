package org.utopia.hackatonsesi_2025.treatments.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "procedure_catalog", indexes = {
        @Index(name = "idx_procedure_code", columnList = "code", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 80, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = 160, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_specialty", length = 40, nullable = false)
    private Specialty requiredSpecialty;

    @Column(name = "min_duration_min", nullable = false)
    private int minDurationMinutes;

    @Column(name = "max_duration_min", nullable = false)
    private int maxDurationMinutes;

    @Column(name = "multi_session", nullable = false)
    @Builder.Default
    private boolean multiSession = false;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "description", length = 1000)
    private String description;
}

