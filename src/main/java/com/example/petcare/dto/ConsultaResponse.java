package com.example.petcare.dto;

import com.example.petcare.model.StatusConsulta;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsultaResponse(
        Long id,
        Long animalId,
        String animalNome,
        String tutorNome,
        LocalDateTime dataHoraAgendada,
        LocalDateTime dataHoraRealizada,
        String motivo,
        String diagnostico,
        StatusConsulta status,
        BigDecimal valor
) {
}
