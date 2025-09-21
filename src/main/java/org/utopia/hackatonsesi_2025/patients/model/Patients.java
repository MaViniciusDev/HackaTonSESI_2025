package org.utopia.hackatonsesi_2025.patients.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "patients",
        indexes = {
                @Index(name = "idx_patients_cpf", columnList = "cpf"),
                @Index(name = "idx_patients_registration", columnList = "registration")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    // Cadastro principal
    @Column(name = "registration", length = 30, unique = true)
    private String registration; // Matrícula

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "sub_type", length = 20)
    private String subType;

    @Column(name = "industry_code", length = 20)
    private String industryCode; // Cód Indust.

    @Column(name = "dependent_code", length = 20)
    private String dependentCode; // Cód Dep.

    @Column(name = "status", length = 20)
    private String status; // Situação

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "access_card", length = 30)
    private String accessCard; // Cartão de Acesso

    // Dados pessoais
    @Column(name = "birth_date")
    private LocalDate birthDate; // Nascimento

    @Column(name = "birth_place", length = 60)
    private String birthPlace; // Naturalidade

    @Column(name = "birth_uf", length = 2)
    private String birthUf; // U.F. de nascimento

    @Column(name = "country", length = 60)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", length = 20)
    private MaritalStatus maritalStatus; // Est. Civil

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", length = 10)
    private Sex sex;

    @Column(name = "profession_cbo", length = 20)
    private String professionCbo; // Profissão/CBO

    @Column(name = "job_function", length = 60)
    private String jobFunction; // Função

    @Column(name = "rg", length = 20)
    private String rg;

    @Column(name = "rg_issuer", length = 20)
    private String rgIssuer; // Órg Exped.

    @Column(name = "short_name", length = 60)
    private String shortName; // Nome Abreviado

    @Column(name = "voter_id", length = 15)
    private String voterId; // Título Eleitor (opcional)

    @Column(name = "pis", length = 14)
    private String pis; // PIS/PASEP (opcional)

    // Endereço
    @Column(name = "address", length = 120)
    private String address;

    @Column(name = "address_number", length = 12)
    private String addressNumber;

    @Column(name = "neighborhood", length = 60)
    private String neighborhood; // Bairro

    @Column(name = "city", length = 60)
    private String city;

    @Column(name = "state", length = 2)
    private String state; // U.F.

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "complement", length = 80)
    private String complement;

    // Empresa/convênio
    @Column(name = "industrial_company", length = 120)
    private String industrialCompany; // Empresa Industrial

    @Column(name = "convenio", length = 60)
    private String convenio;

    @Column(name = "unit_assignment", length = 60)
    private String unitAssignment; // Unidade Lotação

    // Contatos
    @Column(name = "ddd", length = 3)
    private String ddd;

    @Column(name = "phone1", length = 20)
    private String phone1;

    @Column(name = "phone2", length = 20)
    private String phone2;

    @Column(name = "fax", length = 20)
    private String fax;

    // Outros
    @Column(name = "education_level", length = 40)
    private String educationLevel; // Escolaridade

    @Column(name = "salary_range", length = 40)
    private String salaryRange; // Faixa Salarial

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "notes", columnDefinition = "text")
    private String notes; // Observações

    @Column(name = "card_valid_until")
    private LocalDate cardValidUntil; // Validade Carteira

    @Column(name = "is_special", nullable = false)
    private boolean special = false;

    @Column(name = "sesi_acolhe", nullable = false)
    private boolean sesiAcolhe = false;

    @Column(name = "asbt", nullable = false)
    private boolean asbt = false;
}
