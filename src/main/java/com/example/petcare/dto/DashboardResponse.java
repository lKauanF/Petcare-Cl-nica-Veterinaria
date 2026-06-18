package com.example.petcare.dto;

import java.util.List;

public record DashboardResponse(
        long totalAnimais,
        long totalTutores,
        long totalConsultasRealizadas,
        long totalConsultasAgendadas,
        List<ConsultaResponse> proximasConsultas
) {
}
