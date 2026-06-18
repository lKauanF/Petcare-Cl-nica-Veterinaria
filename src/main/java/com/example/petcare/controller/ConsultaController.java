package com.example.petcare.controller;

import com.example.petcare.dto.ConsultaAgendarRequest;
import com.example.petcare.dto.ConsultaRealizarRequest;
import com.example.petcare.dto.ConsultaResponse;
import com.example.petcare.model.StatusConsulta;
import com.example.petcare.service.ConsultaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public List<ConsultaResponse> listar(
            @RequestParam(required = false) Long animalId,
            @RequestParam(required = false) StatusConsulta status
    ) {
        return consultaService.listar(animalId, status);
    }

    @GetMapping("/{id}")
    public ConsultaResponse buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }

    @GetMapping("/ativas")
    public List<ConsultaResponse> listarAtivas() {
        return consultaService.listarAtivas();
    }

    @PostMapping("/agendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse agendar(@Valid @RequestBody ConsultaAgendarRequest request) {
        return consultaService.agendar(request);
    }

    @PutMapping("/{id}/realizar")
    public ConsultaResponse realizar(@PathVariable Long id, @Valid @RequestBody ConsultaRealizarRequest request) {
        return consultaService.realizar(id, request);
    }

    @PutMapping("/{id}/cancelar")
    public ConsultaResponse cancelar(@PathVariable Long id) {
        return consultaService.cancelar(id);
    }
}
