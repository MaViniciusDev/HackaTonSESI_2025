package org.utopia.hackatonsesi_2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.utopia.hackatonsesi_2025.users.security.AppUserDetailsService;
import org.utopia.hackatonsesi_2025.users.security.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            DaoAuthenticationProvider jpaAuthProvider,
                                            DaoAuthenticationProvider inMemoryAuthProvider,
                                            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(inMemoryAuthProvider)
                .authenticationProvider(jpaAuthProvider)
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                        .requestMatchers("/api/procedures/catalog").hasAnyRole("PATIENT","RECEPTION","DENTIST")
                        .requestMatchers("/api/procedures/schedule").hasAnyRole("PATIENT","RECEPTION")
                        .requestMatchers("/api/procedures/orders/**").hasRole("DENTIST")

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

                        .requestMatchers("/api/records/**").hasRole("DENTIST")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> res.sendError(401)))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
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

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
