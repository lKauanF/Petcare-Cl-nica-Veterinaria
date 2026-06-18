package com.example.petcare.dto;

public record TutorResponse(
        Long id,
        String nome,
        String telefone,
        String email,
        String endereco,
        int totalAnimais
) {
}
