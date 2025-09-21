package org.utopia.hackatonsesi_2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.utopia.hackatonsesi_2025.users.security.AppUserDetailsService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            DaoAuthenticationProvider jpaAuthProvider,
                                            DaoAuthenticationProvider inMemoryAuthProvider) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(inMemoryAuthProvider)
                .authenticationProvider(jpaAuthProvider)
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                        .requestMatchers("/api/procedures/catalog").hasAnyRole("PATIENT","RECEPTION","DENTIST")
                        .requestMatchers("/api/procedures/schedule").hasAnyRole("PATIENT","RECEPTION")

                        .requestMatchers("/api/patients/self-register").hasRole("PATIENT")
                        .requestMatchers("/api/patients/intake").hasRole("RECEPTION")
                        .requestMatchers("/api/patients/search", "/api/patients/by-cpf/**", "/api/patients/by-rg/**").hasAnyRole("RECEPTION","DENTIST")
                        .requestMatchers("/api/patients/self/**").hasRole("PATIENT")
                        .requestMatchers("/api/patients/**").hasAnyRole("RECEPTION","DENTIST")

                        .requestMatchers(HttpMethod.GET, "/api/appointments/availability").hasAnyRole("PATIENT","RECEPTION","DENTIST")
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasAnyRole("PATIENT","RECEPTION","DENTIST")
                        .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/reschedule").hasAnyRole("RECEPTION","DENTIST")
                        .requestMatchers(HttpMethod.PATCH, "/api/appointments/*/reschedule-by-patient").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/*/complete").hasRole("DENTIST")
                        .requestMatchers(HttpMethod.POST, "/api/appointments/walk-in").hasAnyRole("RECEPTION","DENTIST")

                        .requestMatchers("/api/appointments/**").hasRole("DENTIST")
                        .requestMatchers(HttpMethod.POST, "/api/referrals").hasRole("DENTIST")
                        .requestMatchers(HttpMethod.GET, "/api/referrals/pending").hasAnyRole("PATIENT","RECEPTION")
                        .requestMatchers(HttpMethod.POST, "/api/referrals/*/schedule").hasAnyRole("PATIENT","RECEPTION")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("paciente").password(encoder.encode("senha")).roles("PATIENT").build(),
                User.withUsername("recepcao").password(encoder.encode("senha")).roles("RECEPTION").build(),
                User.withUsername("dentista").password(encoder.encode("senha")).roles("DENTIST").build()
        );
    }

    @Bean
    DaoAuthenticationProvider jpaAuthProvider(AppUserDetailsService appUserDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    DaoAuthenticationProvider inMemoryAuthProvider(UserDetailsService users, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(users);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
