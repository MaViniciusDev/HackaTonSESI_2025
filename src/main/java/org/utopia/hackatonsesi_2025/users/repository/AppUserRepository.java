package org.utopia.hackatonsesi_2025.users.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.utopia.hackatonsesi_2025.users.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}

