package org.utopia.hackatonsesi_2025.scheduling.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.scheduling.model.Referral;
import org.utopia.hackatonsesi_2025.scheduling.model.ReferralStatus;

public interface ReferralRepository extends JpaRepository<Referral, Long> {

    @EntityGraph(attributePaths = {"patient"})
    List<Referral> findByPatient_CpfAndStatus(String cpf, ReferralStatus status);

    @EntityGraph(attributePaths = {"patient"})
    Optional<Referral> findById(Long id);
}

