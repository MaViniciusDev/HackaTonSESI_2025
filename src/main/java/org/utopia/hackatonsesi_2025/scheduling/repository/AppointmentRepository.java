package org.utopia.hackatonsesi_2025.scheduling.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.scheduling.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByStartBetween(LocalDateTime start, LocalDateTime end, Sort sort);

    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByDentistAndStartBetween(String dentist, LocalDateTime start, LocalDateTime end, Sort sort);

    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByPatient_Cpf(String cpf, Sort sort);

    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByDentistAndPatient_Cpf(String dentist, String cpf, Sort sort);

    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByPatient_NameContainingIgnoreCase(String name, Sort sort);

    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByDentistAndPatient_NameContainingIgnoreCase(String dentist, String name, Sort sort);

    boolean existsByDentistAndStartLessThanAndEndGreaterThan(String dentist, LocalDateTime end, LocalDateTime start);

    boolean existsByDentistAndWalkInFalseAndStartLessThanAndEndGreaterThan(String dentist, LocalDateTime end, LocalDateTime start);

    boolean existsByDentistAndWalkInFalseAndIdNotAndStartLessThanAndEndGreaterThan(String dentist, Long id, LocalDateTime end, LocalDateTime start);

    boolean existsByDentistAndIdNotAndStartLessThanAndEndGreaterThan(String dentist, Long id, LocalDateTime end, LocalDateTime start);
}
