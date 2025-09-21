package org.utopia.hackatonsesi_2025.treatments.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.treatments.model.TreatmentAnalysis;

public interface TreatmentAnalysisRepository extends JpaRepository<TreatmentAnalysis, Long> {
    List<TreatmentAnalysis> findByOrder_IdOrderByCreatedAtAsc(Long orderId);
}

