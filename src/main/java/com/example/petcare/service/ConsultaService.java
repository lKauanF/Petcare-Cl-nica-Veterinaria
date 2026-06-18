package com.example.petcare.service;

import com.example.petcare.dto.ConsultaAgendarRequest;
import com.example.petcare.dto.ConsultaRealizarRequest;
import com.example.petcare.dto.ConsultaResponse;
import com.example.petcare.exception.BusinessException;
import com.example.petcare.exception.ResourceNotFoundException;
import com.example.petcare.mapper.PetCareMapper;
import com.example.petcare.model.Animal;
import com.example.petcare.model.Consulta;
import com.example.petcare.model.StatusConsulta;
import com.example.petcare.repository.ConsultaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final AnimalService animalService;

    public ConsultaService(ConsultaRepository consultaRepository, AnimalService animalService) {
        this.consultaRepository = consultaRepository;
        this.animalService = animalService;
    }

    public List<ConsultaResponse> listar(Long animalId, StatusConsulta status) {
        List<Consulta> consultas;

        if (animalId != null) {
            consultas = consultaRepository.findByAnimalId(animalId);
        } else if (status != null) {
            consultas = consultaRepository.findByStatus(status);
        } else {
            consultas = consultaRepository.findAll();
        }

        return consultas.stream()
                .filter(consulta -> status == null || consulta.getStatus() == status)
                .map(PetCareMapper::toConsultaResponse)
                .toList();
    }

    public List<ConsultaResponse> listarAtivas() {
        return consultaRepository.findByStatus(StatusConsulta.AGENDADA).stream()
                .map(PetCareMapper::toConsultaResponse)
                .toList();
    }

    public Consulta buscarEntidadePorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta nao encontrada."));
    }

    public ConsultaResponse buscarPorId(Long id) {
        return PetCareMapper.toConsultaResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public ConsultaResponse agendar(ConsultaAgendarRequest request) {
        Animal animal = animalService.buscarEntidadePorId(request.animalId());

        Consulta consulta = new Consulta();
        consulta.setAnimal(animal);
        consulta.setDataHoraAgendada(request.dataHoraAgendada());
        consulta.setMotivo(request.motivo());
        consulta.setValor(request.valor());
        consulta.setStatus(StatusConsulta.AGENDADA);

        return PetCareMapper.toConsultaResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaResponse realizar(Long id, ConsultaRealizarRequest request) {
        Consulta consulta = buscarEntidadePorId(id);
        validarConsultaAgendada(consulta);

        consulta.setDiagnostico(request.diagnostico());
        consulta.setDataHoraRealizada(LocalDateTime.now());
        consulta.setStatus(StatusConsulta.REALIZADA);

        return PetCareMapper.toConsultaResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaResponse cancelar(Long id) {
        Consulta consulta = buscarEntidadePorId(id);
        validarConsultaAgendada(consulta);

        consulta.setStatus(StatusConsulta.CANCELADA);

        return PetCareMapper.toConsultaResponse(consultaRepository.save(consulta));
    }

    public List<ConsultaResponse> buscarProximasConsultas() {
        return consultaRepository
                .findTop5ByStatusAndDataHoraAgendadaAfterOrderByDataHoraAgendadaAsc(StatusConsulta.AGENDADA, LocalDateTime.now())
                .stream()
                .map(PetCareMapper::toConsultaResponse)
                .toList();
    }

    private void validarConsultaAgendada(Consulta consulta) {
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new BusinessException("Apenas consultas agendadas podem ser alteradas.");
        }
    }
}
