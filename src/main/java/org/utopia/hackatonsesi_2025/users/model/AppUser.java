package org.utopia.hackatonsesi_2025.users.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import org.utopia.hackatonsesi_2025.patients.model.Patients;

@Entity
@Table(name = "app_users", indexes = {
        @Index(name = "idx_app_users_cpf", columnList = "cpf", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;

    @ToString.Exclude
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patients patient; // opcional: linka com o cadastro do paciente se existir
}
