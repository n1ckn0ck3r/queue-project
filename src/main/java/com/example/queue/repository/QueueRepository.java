package com.example.queue.repository;

import com.example.queue.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByDisciplineId(Long disciplineId);
}
