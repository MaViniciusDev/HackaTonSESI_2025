package org.utopia.hackatonsesi_2025.patients.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utopia.hackatonsesi_2025.patients.dto.PatientIntakeDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientReceptionUpdateDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientResponseDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientSelfRegisterDTO;
import org.utopia.hackatonsesi_2025.patients.dto.PatientSelfUpdateDTO;
import org.utopia.hackatonsesi_2025.patients.model.Patients;
import org.utopia.hackatonsesi_2025.patients.model.Sex;
import org.utopia.hackatonsesi_2025.patients.repository.PatientRepository;
import org.utopia.hackatonsesi_2025.users.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final AppUserRepository userRepository;

    @Transactional
    public PatientResponseDTO createFromSelfRegister(PatientSelfRegisterDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        Patients p = Patients.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .birthDate(dto.birthDate())
                .sex(Sex.valueOf(dto.sex().name()))
                .email(dto.email())
                .ddd(dto.ddd())
                .phone1(dto.phone1())
                .address(dto.address())
                .addressNumber(dto.addressNumber())
                .neighborhood(dto.neighborhood())
                .city(dto.city())
                .state(dto.state())
                .cep(dto.cep())
                .build();
        final Patients saved = repository.save(p);
        // vincula AppUser se existir e ainda não estiver vinculado
        userRepository.findByCpf(saved.getCpf()).ifPresent(u -> {
            if (u.getPatient() == null) { u.setPatient(saved); userRepository.save(u); }
        });
        return toResponse(saved);
    }

    @Transactional
    public PatientResponseDTO createFromReception(PatientIntakeDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        Patients p = Patients.builder()
                .name(dto.name())
                .registration(dto.registration())
                .status(dto.status())
                .cpf(dto.cpf())
                .birthDate(dto.birthDate())
                .sex(Sex.valueOf(dto.sex().name()))
                .rg(dto.rg())
                .rgIssuer(dto.rgIssuer())
                .industrialCompany(dto.industrialCompany())
                .convenio(dto.convenio())
                .unitAssignment(dto.unitAssignment())
                .ddd(dto.ddd())
                .phone1(dto.phone1())
                .email(dto.email())
                .build();
        final Patients saved = repository.save(p);
        // vincula AppUser se existir e ainda não estiver vinculado
        userRepository.findByCpf(saved.getCpf()).ifPresent(u -> {
            if (u.getPatient() == null) { u.setPatient(saved); userRepository.save(u); }
        });
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO getByCpf(String cpf) {
        Patients p = repository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo CPF"));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO getByRg(String rg) {
        Patients p = repository.findByRg(rg)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo RG"));
        return toResponse(p);
    }

    @Transactional
    public PatientResponseDTO updateSelf(String cpf, PatientSelfUpdateDTO dto) {
        Patients p = repository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado pelo CPF"));
        if (dto.name() != null) p.setName(dto.name());
        if (dto.birthDate() != null) p.setBirthDate(dto.birthDate());
        if (dto.sex() != null) p.setSex(dto.sex());
        if (dto.email() != null) p.setEmail(dto.email());
        if (dto.ddd() != null) p.setDdd(dto.ddd());
        if (dto.phone1() != null) p.setPhone1(dto.phone1());
        if (dto.address() != null) p.setAddress(dto.address());
        if (dto.addressNumber() != null) p.setAddressNumber(dto.addressNumber());
        if (dto.neighborhood() != null) p.setNeighborhood(dto.neighborhood());
        if (dto.city() != null) p.setCity(dto.city());
        if (dto.state() != null) p.setState(dto.state());
        if (dto.cep() != null) p.setCep(dto.cep());
        return toResponse(repository.save(p));
    }

    @Transactional
    public PatientResponseDTO updateFromReception(Long id, PatientReceptionUpdateDTO dto) {
        Patients p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        if (dto.name() != null) p.setName(dto.name());
        if (dto.registration() != null) p.setRegistration(dto.registration());
        if (dto.status() != null) p.setStatus(dto.status());
        if (dto.birthDate() != null) p.setBirthDate(dto.birthDate());
        if (dto.sex() != null) p.setSex(dto.sex());
        if (dto.rg() != null) p.setRg(dto.rg());
        if (dto.rgIssuer() != null) p.setRgIssuer(dto.rgIssuer());
        if (dto.industrialCompany() != null) p.setIndustrialCompany(dto.industrialCompany());
        if (dto.convenio() != null) p.setConvenio(dto.convenio());
        if (dto.unitAssignment() != null) p.setUnitAssignment(dto.unitAssignment());
        if (dto.ddd() != null) p.setDdd(dto.ddd());
        if (dto.phone1() != null) p.setPhone1(dto.phone1());
        if (dto.email() != null) p.setEmail(dto.email());
        if (dto.address() != null) p.setAddress(dto.address());
        if (dto.addressNumber() != null) p.setAddressNumber(dto.addressNumber());
        if (dto.neighborhood() != null) p.setNeighborhood(dto.neighborhood());
        if (dto.city() != null) p.setCity(dto.city());
        if (dto.state() != null) p.setState(dto.state());
        if (dto.cep() != null) p.setCep(dto.cep());
        if (dto.notes() != null) p.setNotes(dto.notes());
        return toResponse(repository.save(p));
    }

    private PatientResponseDTO toResponse(Patients p) {
        return new PatientResponseDTO(p.getId(), p.getName(), p.getCpf(), p.getBirthDate(), p.getEmail());
    }
}
