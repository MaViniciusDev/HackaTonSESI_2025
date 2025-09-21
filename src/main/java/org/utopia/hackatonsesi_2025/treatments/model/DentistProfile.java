package org.utopia.hackatonsesi_2025.treatments.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "dentist_profiles", indexes = {
        @Index(name = "idx_dentist_username", columnList = "dentist_username", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DentistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dentist_username", length = 120, nullable = false, unique = true)
    private String dentistUsername;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dentist_specialties", joinColumns = @JoinColumn(name = "profile_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "specialty", length = 40, nullable = false)
    @Builder.Default
    private Set<Specialty> specialties = new HashSet<>();
}

