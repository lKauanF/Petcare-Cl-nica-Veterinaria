package com.example.petcare.controller;

import com.example.petcare.dto.AnimalRequest;
import com.example.petcare.dto.AnimalResponse;
import com.example.petcare.model.Especie;
import com.example.petcare.service.AnimalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/animais")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalResponse> listar(
            @RequestParam(required = false) Especie especie,
            @RequestParam(required = false) Long tutorId,
            @RequestParam(required = false) String busca
    ) {
        return animalService.listar(especie, tutorId, busca);
    }

    @GetMapping("/{id}")
    public AnimalResponse buscarPorId(@PathVariable Long id) {
        return animalService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse criar(@Valid @RequestBody AnimalRequest request) {
        return animalService.criar(request);
    }

    @PutMapping("/{id}")
    public AnimalResponse editar(@PathVariable Long id, @Valid @RequestBody AnimalRequest request) {
        return animalService.editar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        animalService.excluir(id);
    }
}
