package com.example.petcare.dto;

import com.example.petcare.model.Especie;
import java.time.LocalDate;

public record AnimalResponse(
        Long id,
        String nome,
        Especie especie,
        String raca,
        LocalDate dataNascimento,
        Long tutorId,
        String tutorNome
) {
}
