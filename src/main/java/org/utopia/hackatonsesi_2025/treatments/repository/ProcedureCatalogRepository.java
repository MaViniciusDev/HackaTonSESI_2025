package org.utopia.hackatonsesi_2025.treatments.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.treatments.model.ProcedureCatalog;
import org.utopia.hackatonsesi_2025.treatments.model.Specialty;

public interface ProcedureCatalogRepository extends JpaRepository<ProcedureCatalog, Long> {
    Optional<ProcedureCatalog> findByCode(String code);
    List<ProcedureCatalog> findByRequiredSpecialty(Specialty specialty);
}

