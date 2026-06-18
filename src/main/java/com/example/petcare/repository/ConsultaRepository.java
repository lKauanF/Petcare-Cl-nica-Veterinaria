package com.example.petcare.repository;

import com.example.petcare.model.Consulta;
import com.example.petcare.model.StatusConsulta;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByAnimalId(Long animalId);

    List<Consulta> findByAnimalId(Long animalId);

    List<Consulta> findByStatus(StatusConsulta status);

    List<Consulta> findTop5ByStatusAndDataHoraAgendadaAfterOrderByDataHoraAgendadaAsc(StatusConsulta status, LocalDateTime dataHora);
}
