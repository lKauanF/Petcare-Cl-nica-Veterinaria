package com.example.petcare.dto;

import com.example.petcare.model.Especie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AnimalRequest(
        @NotBlank String nome,
        @NotNull Especie especie,
        @NotBlank String raca,
        @NotNull LocalDate dataNascimento,
        @NotNull Long tutorId
) {
}
