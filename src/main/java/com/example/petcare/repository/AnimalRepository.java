package com.example.petcare.repository;

import com.example.petcare.model.Animal;
import com.example.petcare.model.Especie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByEspecie(Especie especie);

    List<Animal> findByTutorId(Long tutorId);

    @Query("""
            select a from Animal a
            join a.tutor t
            where lower(a.nome) like lower(concat('%', :busca, '%'))
               or lower(t.nome) like lower(concat('%', :busca, '%'))
            """)
    List<Animal> buscarPorNomeAnimalOuTutor(@Param("busca") String busca);
}
