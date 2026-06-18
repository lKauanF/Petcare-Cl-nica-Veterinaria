package com.example.petcare.repository;

import com.example.petcare.model.Tutor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Tutor> findByEmailIgnoreCase(String email);
}
