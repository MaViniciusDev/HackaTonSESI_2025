package org.utopia.hackatonsesi_2025.patients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.patients.model.Patients;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patients, Long> {
    Optional<Patients> findByCpf(String cpf);
    boolean existsByCpf(String cpf);

    Optional<Patients> findByRg(String rg);
    boolean existsByRg(String rg);

    List<Patients> findByNameContainingIgnoreCase(String name);

    boolean existsByRegistration(String registration);
}
