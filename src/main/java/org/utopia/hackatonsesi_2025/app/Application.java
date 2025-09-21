package org.utopia.hackatonsesi_2025.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.utopia")
@EnableJpaRepositories(basePackages = {
        "org.utopia.hackatonsesi_2025.patients.repository",
        "org.utopia.hackatonsesi_2025.scheduling.repository",
        "org.utopia.hackatonsesi_2025.users.repository",
        "org.utopia.hackatonsesi_2025.treatments.repository"
})
@EntityScan(basePackages = {
        "org.utopia.hackatonsesi_2025.patients.model",
        "org.utopia.hackatonsesi_2025.scheduling.model",
        "org.utopia.hackatonsesi_2025.users.model",
        "org.utopia.hackatonsesi_2025.treatments.model"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}