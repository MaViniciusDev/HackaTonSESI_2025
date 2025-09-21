package org.utopia.hackatonsesi_2025.users.security;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.utopia.hackatonsesi_2025.users.model.AppUser;
import org.utopia.hackatonsesi_2025.users.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        AppUser user = repository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.<GrantedAuthority>toList());
        return User.withUsername(user.getCpf())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountLocked(!user.isEnabled())
                .disabled(!user.isEnabled())
                .build();
    }
}

