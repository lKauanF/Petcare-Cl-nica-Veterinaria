package com.example.petcare.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsultaAgendarRequest(
        @NotNull Long animalId,
        @NotNull LocalDateTime dataHoraAgendada,
        @NotBlank String motivo,
        @NotNull @DecimalMin("0.0") BigDecimal valor
) {
}
