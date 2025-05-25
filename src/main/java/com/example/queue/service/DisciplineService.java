package com.example.queue.service;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.exception.BadRequestException;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.repository.DisciplineRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;
    private final ObjectMapper objectMapper;

    public List<DisciplineDto> getDisciplines() {
        return DisciplineDto.fromList(disciplineRepository.findAll());
    }

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

    public DisciplineDto updateDisciplineById(Long id, Map<String, Object> updates) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new NotFoundException("Дисциплины с таким id не существует"));

        try {
            objectMapper.updateValue(discipline, updates);
        } catch (JsonMappingException e) {
            throw new BadRequestException(e.getMessage());
        }

        disciplineRepository.save(discipline);
        return DisciplineDto.from(discipline);
    }

    public DisciplineDto updateDisciplineTotallyById(Long id, DisciplineDto updates) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new NotFoundException("Дисциплины с таким id не существует"));
        discipline.setDisciplineName(updates.getDisciplineName());
        disciplineRepository.save(discipline);
        return DisciplineDto.from(discipline);
    }

    public DisciplineDto deleteDisciplineById(Long id) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new NotFoundException("Дисциплины с таким id не существует"));
        disciplineRepository.delete(discipline);
        return DisciplineDto.from(discipline);
    }
}
