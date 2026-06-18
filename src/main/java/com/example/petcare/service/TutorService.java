package com.example.petcare.service;

import com.example.petcare.dto.TutorRequest;
import com.example.petcare.dto.TutorResponse;
import com.example.petcare.exception.BusinessException;
import com.example.petcare.exception.ResourceNotFoundException;
import com.example.petcare.mapper.PetCareMapper;
import com.example.petcare.model.Tutor;
import com.example.petcare.repository.TutorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TutorService {
    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<TutorResponse> listar() {
        return tutorRepository.findAll().stream()
                .map(PetCareMapper::toTutorResponse)
                .toList();
    }

    public Tutor buscarEntidadePorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor nao encontrado."));
    }

    public TutorResponse buscarPorId(Long id) {
        return PetCareMapper.toTutorResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public TutorResponse criar(TutorRequest request) {
        validarEmailUnico(request.email(), null);

        Tutor tutor = new Tutor();
        tutor.setNome(request.nome());
        tutor.setTelefone(request.telefone());
        tutor.setEmail(request.email());
        tutor.setEndereco(request.endereco());

        return PetCareMapper.toTutorResponse(tutorRepository.save(tutor));
    }

    @Transactional
    public TutorResponse editar(Long id, TutorRequest request) {
        Tutor tutor = buscarEntidadePorId(id);
        validarEmailUnico(request.email(), id);

        tutor.setNome(request.nome());
        tutor.setTelefone(request.telefone());
        tutor.setEmail(request.email());
        tutor.setEndereco(request.endereco());

        return PetCareMapper.toTutorResponse(tutorRepository.save(tutor));
    }

    @Transactional
    public void excluir(Long id) {
        Tutor tutor = buscarEntidadePorId(id);

        if (!tutor.getAnimais().isEmpty()) {
            throw new BusinessException("Nao e permitido excluir tutor com animais vinculados.");
        }

        tutorRepository.delete(tutor);
    }

    private void validarEmailUnico(String email, Long idAtual) {
        tutorRepository.findByEmailIgnoreCase(email).ifPresent(tutor -> {
            if (idAtual == null || !tutor.getId().equals(idAtual)) {
                throw new BusinessException("Ja existe tutor cadastrado com este email.");
            }
        });
    }
}
