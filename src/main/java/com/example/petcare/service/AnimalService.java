package com.example.petcare.service;

import com.example.petcare.dto.AnimalRequest;
import com.example.petcare.dto.AnimalResponse;
import com.example.petcare.exception.BusinessException;
import com.example.petcare.exception.ResourceNotFoundException;
import com.example.petcare.mapper.PetCareMapper;
import com.example.petcare.model.Animal;
import com.example.petcare.model.Especie;
import com.example.petcare.model.Tutor;
import com.example.petcare.repository.AnimalRepository;
import com.example.petcare.repository.ConsultaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ConsultaRepository consultaRepository;
    private final TutorService tutorService;

    public AnimalService(AnimalRepository animalRepository, ConsultaRepository consultaRepository, TutorService tutorService) {
        this.animalRepository = animalRepository;
        this.consultaRepository = consultaRepository;
        this.tutorService = tutorService;
    }

    public List<AnimalResponse> listar(Especie especie, Long tutorId, String busca) {
        List<Animal> animais;

        if (busca != null && !busca.isBlank()) {
            animais = animalRepository.buscarPorNomeAnimalOuTutor(busca);
        } else if (especie != null) {
            animais = animalRepository.findByEspecie(especie);
        } else if (tutorId != null) {
            animais = animalRepository.findByTutorId(tutorId);
        } else {
            animais = animalRepository.findAll();
        }

        return animais.stream()
                .filter(animal -> especie == null || animal.getEspecie() == especie)
                .filter(animal -> tutorId == null || animal.getTutor().getId().equals(tutorId))
                .map(PetCareMapper::toAnimalResponse)
                .toList();
    }

    public Animal buscarEntidadePorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal nao encontrado."));
    }

    public AnimalResponse buscarPorId(Long id) {
        return PetCareMapper.toAnimalResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public AnimalResponse criar(AnimalRequest request) {
        Tutor tutor = tutorService.buscarEntidadePorId(request.tutorId());

        Animal animal = new Animal();
        preencherAnimal(animal, request, tutor);

        return PetCareMapper.toAnimalResponse(animalRepository.save(animal));
    }

    @Transactional
    public AnimalResponse editar(Long id, AnimalRequest request) {
        Animal animal = buscarEntidadePorId(id);
        Tutor tutor = tutorService.buscarEntidadePorId(request.tutorId());

        preencherAnimal(animal, request, tutor);

        return PetCareMapper.toAnimalResponse(animalRepository.save(animal));
    }

    @Transactional
    public void excluir(Long id) {
        Animal animal = buscarEntidadePorId(id);

        if (consultaRepository.existsByAnimalId(id)) {
            throw new BusinessException("Nao e permitido excluir animal com consultas vinculadas.");
        }

        animalRepository.delete(animal);
    }

    private void preencherAnimal(Animal animal, AnimalRequest request, Tutor tutor) {
        animal.setNome(request.nome());
        animal.setEspecie(request.especie());
        animal.setRaca(request.raca());
        animal.setDataNascimento(request.dataNascimento());
        animal.setTutor(tutor);
    }
}
