package com.example.queue.service;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.dto.GroupDisciplineDto;
import com.example.queue.dto.GroupDto;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.model.Group;
import com.example.queue.repository.DisciplineRepository;
import com.example.queue.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupDisciplineService {
    private final GroupRepository groupRepository;
    private final DisciplineRepository disciplineRepository;

    public GroupDisciplineDto linkGroupAndDiscipline(GroupDisciplineDto groupDisciplineDto) {
        Group group = groupRepository.findById(groupDisciplineDto.getGroupId()).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        Discipline discipline = disciplineRepository.findById(groupDisciplineDto.getDisciplineId()).orElseThrow(() -> new NotFoundException("Дисциплина с таким id не найдена"));

        group.getDisciplines().add(discipline);
        discipline.getGroups().add(group);
        groupRepository.save(group);

        return new GroupDisciplineDto(GroupDto.from(group), DisciplineDto.from(discipline));
    }

    public List<DisciplineDto> getDisciplinesByGroupId(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        return DisciplineDto.fromSet(group.getDisciplines());
    }

    public List<GroupDto> getGroupsByDisciplineId(Long id) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new NotFoundException("Дисциплина с таким id не найдена"));
        return GroupDto.fromSet(discipline.getGroups());
    }
}
