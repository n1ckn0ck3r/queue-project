package com.example.queue.repository;

import com.example.queue.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    Optional<Discipline> findByDisciplineName(String disciplineName);
}
