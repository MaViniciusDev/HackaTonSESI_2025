package org.utopia.hackatonsesi_2025.treatments.config;

import java.util.List;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.utopia.hackatonsesi_2025.treatments.model.*;
import org.utopia.hackatonsesi_2025.treatments.repository.*;

@Configuration
public class TreatmentsDataSeeder {

    @Bean
    CommandLineRunner seedProcedures(ProcedureCatalogRepository catalogRepo, DentistProfileRepository dentistRepo) {
        return args -> {
            if (catalogRepo.count() == 0) {
                catalogRepo.saveAll(List.of(
                        // Clínico Geral
                        ProcedureCatalog.builder().code("CHECKUP").name("Consulta de avaliação / check-up").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(20).maxDurationMinutes(30).build(),
                        ProcedureCatalog.builder().code("PROFILAXIA").name("Profilaxia (limpeza)").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(30).maxDurationMinutes(45).build(),
                        ProcedureCatalog.builder().code("FLUOR").name("Aplicação de flúor").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(10).maxDurationMinutes(15).build(),
                        ProcedureCatalog.builder().code("CLAREAMENTO").name("Clareamento em consultório").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(60).maxDurationMinutes(90).build(),
                        ProcedureCatalog.builder().code("RESTAURACAO_SIMPLES").name("Restauração simples (1 dente)").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(30).maxDurationMinutes(40).build(),
                        ProcedureCatalog.builder().code("RESTAURACAO_MULTIPLA").name("Restauração múltipla / mais complexa").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(45).maxDurationMinutes(60).build(),
                        ProcedureCatalog.builder().code("EXTRACAO_SIMPLES").name("Extração simples").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(30).maxDurationMinutes(45).build(),
                        ProcedureCatalog.builder().code("PROTESE_SIMPLES").name("Prótese simples (coroa unitária, ponte pequena)").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(45).maxDurationMinutes(60).build(),
                        ProcedureCatalog.builder().code("CANAL_SIMPLES").name("Tratamento de canal simples").requiredSpecialty(Specialty.CLINICO_GERAL).minDurationMinutes(60).maxDurationMinutes(90).multiSession(true).build(),
                        // Especialidades
                        ProcedureCatalog.builder().code("EXTRACAO_SISO_COMPLEXA").name("Extração de siso incluso / cirurgia complexa").requiredSpecialty(Specialty.CIRURGIA_BUCOMAXILOFACIAL).minDurationMinutes(45).maxDurationMinutes(90).build(),
                        ProcedureCatalog.builder().code("CANAL_COMPLEXO").name("Tratamento de canal complexo").requiredSpecialty(Specialty.ENDODONTIA).minDurationMinutes(60).maxDurationMinutes(90).multiSession(true).build(),
                        ProcedureCatalog.builder().code("IMPLANTE").name("Implante dentário").requiredSpecialty(Specialty.IMPLANTODONTIA).minDurationMinutes(60).maxDurationMinutes(120).build(),
                        ProcedureCatalog.builder().code("PROTESE_AVANCADA").name("Prótese dentária avançada (dentadura, coroas múltiplas)").requiredSpecialty(Specialty.PROTESE).minDurationMinutes(60).maxDurationMinutes(90).build(),
                        ProcedureCatalog.builder().code("ORTO_INSTALACAO").name("Ortodontia – instalação de aparelho").requiredSpecialty(Specialty.ORTODONTIA).minDurationMinutes(60).maxDurationMinutes(90).build(),
                        ProcedureCatalog.builder().code("ORTO_MANUTENCAO").name("Ortodontia – manutenção de aparelho").requiredSpecialty(Specialty.ORTODONTIA).minDurationMinutes(15).maxDurationMinutes(30).build(),
                        ProcedureCatalog.builder().code("PERIODONTIA_AVANCADA").name("Periodontia – tratamento de doença gengival avançada").requiredSpecialty(Specialty.PERIODONTIA).minDurationMinutes(45).maxDurationMinutes(60).build(),
                        ProcedureCatalog.builder().code("ODONTOPEDIATRIA").name("Odontopediatria – atendimento infantil especializado").requiredSpecialty(Specialty.ODONTOPEDIATRIA).minDurationMinutes(30).maxDurationMinutes(45).build(),
                        ProcedureCatalog.builder().code("CIRURGIA_ORAL_AVANCADA").name("Cirurgia oral avançada / trauma facial").requiredSpecialty(Specialty.CIRURGIA_BUCOMAXILOFACIAL).minDurationMinutes(60).maxDurationMinutes(120).build(),
                        ProcedureCatalog.builder().code("ESTETICA_AVANCADA").name("Estética avançada (facetas/lentes de contato)").requiredSpecialty(Specialty.ESTETICA).minDurationMinutes(60).maxDurationMinutes(120).build()
                ));
            }
            // criar um perfil básico de dentista "dentista" se não existir, com várias especialidades para testes
            if (!dentistRepo.existsByDentistUsername("dentista")) {
                dentistRepo.save(DentistProfile.builder()
                        .dentistUsername("dentista")
                        .specialties(Set.of(
                                Specialty.CLINICO_GERAL,
                                Specialty.ENDODONTIA,
                                Specialty.ORTODONTIA,
                                Specialty.PROTESE,
                                Specialty.CIRURGIA_BUCOMAXILOFACIAL,
                                Specialty.ESTETICA,
                                Specialty.PERIODONTIA
                        ))
                        .build());
            }
        };
    }
}

