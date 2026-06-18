package com.example.petcare.service;

import com.example.petcare.dto.DashboardResponse;
import com.example.petcare.model.StatusConsulta;
import com.example.petcare.repository.AnimalRepository;
import com.example.petcare.repository.ConsultaRepository;
import com.example.petcare.repository.TutorRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final AnimalRepository animalRepository;
    private final TutorRepository tutorRepository;
    private final ConsultaRepository consultaRepository;
    private final ConsultaService consultaService;

    public DashboardService(
            AnimalRepository animalRepository,
            TutorRepository tutorRepository,
            ConsultaRepository consultaRepository,
            ConsultaService consultaService
    ) {
        this.animalRepository = animalRepository;
        this.tutorRepository = tutorRepository;
        this.consultaRepository = consultaRepository;
        this.consultaService = consultaService;
    }

    public DashboardResponse gerarDashboard() {
        return new DashboardResponse(
                animalRepository.count(),
                tutorRepository.count(),
                consultaRepository.findByStatus(StatusConsulta.REALIZADA).size(),
                consultaRepository.findByStatus(StatusConsulta.AGENDADA).size(),
                consultaService.buscarProximasConsultas()
        );
    }
}
