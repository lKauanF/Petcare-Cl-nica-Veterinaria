package com.example.petcare.mapper;

import com.example.petcare.dto.AnimalResponse;
import com.example.petcare.dto.ConsultaResponse;
import com.example.petcare.dto.TutorResponse;
import com.example.petcare.model.Animal;
import com.example.petcare.model.Consulta;
import com.example.petcare.model.Tutor;

public final class PetCareMapper {
    private PetCareMapper() {
    }

    public static TutorResponse toTutorResponse(Tutor tutor) {
        return new TutorResponse(
                tutor.getId(),
                tutor.getNome(),
                tutor.getTelefone(),
                tutor.getEmail(),
                tutor.getEndereco(),
                tutor.getAnimais().size()
        );
    }

    public static AnimalResponse toAnimalResponse(Animal animal) {
        return new AnimalResponse(
                animal.getId(),
                animal.getNome(),
                animal.getEspecie(),
                animal.getRaca(),
                animal.getDataNascimento(),
                animal.getTutor().getId(),
                animal.getTutor().getNome()
        );
    }

    public static ConsultaResponse toConsultaResponse(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getId(),
                consulta.getAnimal().getId(),
                consulta.getAnimal().getNome(),
                consulta.getAnimal().getTutor().getNome(),
                consulta.getDataHoraAgendada(),
                consulta.getDataHoraRealizada(),
                consulta.getMotivo(),
                consulta.getDiagnostico(),
                consulta.getStatus(),
                consulta.getValor()
        );
    }
}
