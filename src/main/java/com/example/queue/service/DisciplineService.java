package com.example.queue.service;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;

    public DisciplineDto addDiscipline(DisciplineDto disciplineDto) {
        if (disciplineRepository.findByDisciplineName(disciplineDto.getDisciplineName()).isPresent()) {
            throw new ConflictException("Дисциплина с таким названием уже существует");
        }

        Discipline discipline = new Discipline(disciplineDto.getDisciplineName());
        disciplineRepository.save(discipline);

        return new DisciplineDto(discipline.getId(), discipline.getDisciplineName());
    }

    public DisciplineDto getDisciplineById(Long id) {
        return DisciplineDto.from(disciplineRepository.findById(id).orElseThrow(() -> new NotFoundException("Дисциплины с таким id не существует")));
    }
}
