package com.example.petcare.dto;

import jakarta.validation.constraints.NotBlank;

public record ConsultaRealizarRequest(
        @NotBlank String diagnostico
) {
}
