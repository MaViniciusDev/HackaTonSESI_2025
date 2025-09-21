package org.utopia.hackatonsesi_2025.treatments.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.treatments.model.DentistProfile;

public interface DentistProfileRepository extends JpaRepository<DentistProfile, Long> {
    Optional<DentistProfile> findByDentistUsername(String dentistUsername);
    boolean existsByDentistUsername(String dentistUsername);
}

