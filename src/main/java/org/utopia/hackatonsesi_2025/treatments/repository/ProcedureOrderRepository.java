package org.utopia.hackatonsesi_2025.treatments.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.treatments.model.ProcedureOrder;
import org.utopia.hackatonsesi_2025.treatments.model.ProcedureOrderStatus;

public interface ProcedureOrderRepository extends JpaRepository<ProcedureOrder, Long> {
    List<ProcedureOrder> findByPatient_CpfAndStatus(String cpf, ProcedureOrderStatus status);
}

