package org.utopia.hackatonsesi_2025.treatments.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.treatments.model.DentistProfile;
import org.utopia.hackatonsesi_2025.treatments.model.ProcedureCatalog;
import org.utopia.hackatonsesi_2025.treatments.model.Specialty;
import org.utopia.hackatonsesi_2025.treatments.repository.DentistProfileRepository;
import org.utopia.hackatonsesi_2025.treatments.repository.ProcedureCatalogRepository;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ProcedureDataInitializer {

    private final ProcedureCatalogRepository procedures;
    private final DentistProfileRepository dentists;

    @PostConstruct
    @Transactional
    public void init() {
        if (procedures.count() == 0) {
            // Catálogo básico (MVP)
            save("CONS_AVAL", "Consulta de avaliação / check-up", Specialty.CLINICO_GERAL, 20, 30, false);
            save("PROFILAXIA", "Profilaxia (limpeza)", Specialty.CLINICO_GERAL, 30, 45, false);
            save("FLUOR", "Aplicação de flúor", Specialty.CLINICO_GERAL, 10, 15, false);
            save("CLAR_CONS", "Clareamento em consultório", Specialty.CLINICO_GERAL, 60, 90, false);
            save("REST_SIMPL", "Restauração simples (1 dente)", Specialty.CLINICO_GERAL, 30, 40, false);
            save("REST_COMPLEX", "Restauração múltipla / mais complexa", Specialty.CLINICO_GERAL, 45, 60, false);
            save("EXT_SIMP", "Extração simples", Specialty.CLINICO_GERAL, 30, 45, false);
            save("PROTESE_SIMPL", "Prótese simples (coroa unitária, ponte pequena)", Specialty.PROTESE, 45, 60, false);
            save("CANAL_SIMP", "Tratamento de canal simples", Specialty.ENDODONTIA, 60, 90, true);
            save("SISO_CIRURG", "Extração de siso incluso / cirurgia complexa", Specialty.CIRURGIA_BUCOMAXILOFACIAL, 45, 90, false);
            save("CANAL_COMPLEX", "Tratamento de canal complexo", Specialty.ENDODONTIA, 60, 90, true);
            save("IMPLANTE", "Implante dentário", Specialty.IMPLANTODONTIA, 60, 120, false);
            save("PROTESE_AVANC", "Prótese dentária avançada (dentadura, coroas múltiplas)", Specialty.PROTESE, 60, 90, false);
            save("ORTO_INST", "Ortodontia – instalação de aparelho", Specialty.ORTODONTIA, 60, 90, false);
            save("ORTO_MANT", "Ortodontia – manutenção de aparelho", Specialty.ORTODONTIA, 15, 30, false);
            save("PERIO_TRAT", "Periodontia – tratamento de doença gengival avançada", Specialty.PERIODONTIA, 45, 60, false);
            save("ODONTOPED", "Odontopediatria – atendimento infantil especializado", Specialty.ODONTOPEDIATRIA, 30, 45, false);
            save("CIR_ORAL_AV", "Cirurgia oral avançada / trauma facial", Specialty.CIRURGIA_BUCOMAXILOFACIAL, 60, 120, false);
            save("ESTETICA_AV", "Estética avançada (facetas/lentes de contato)", Specialty.ESTETICA, 60, 120, false);
        }
        dentists.findByDentistUsername("11111111111").orElseGet(() ->
                dentists.save(DentistProfile.builder()
                        .dentistUsername("11111111111")
                        .specialties(Set.of(
                                Specialty.CLINICO_GERAL,
                                Specialty.ENDODONTIA,
                                Specialty.PROTESE,
                                Specialty.ORTODONTIA
                        ))
                        .build())
        );
    }

    private void save(String code, String name, Specialty s, int min, int max, boolean multi) {
        procedures.save(ProcedureCatalog.builder()
                .code(code)
                .name(name)
                .requiredSpecialty(s)
                .minDurationMinutes(min)
                .maxDurationMinutes(max)
                .multiSession(multi)
                .active(true)
                .build());
    }
}
