package com.example.queue.service;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.dto.GroupDisciplineDto;
import com.example.queue.dto.GroupDto;
import com.example.queue.dto.UserDto;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.model.Group;
import com.example.queue.model.User;
import com.example.queue.repository.DisciplineRepository;
import com.example.queue.repository.GroupRepository;
import com.example.queue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final DisciplineRepository disciplineRepository;

    public GroupDto addGroup(GroupDto groupDto) {
        if (groupRepository.findByGroupName(groupDto.getGroupName()).isPresent()) {
            throw new ConflictException("Группа с таким названием уже существует");
        }

        Group group = new Group(groupDto.getGroupName());
        groupRepository.save(group);

        return new GroupDto(group.getId(), group.getGroupName(), UserDto.fromList(group.getUsers()));
    }

    public GroupDto getGroupById(Long id) {
        return GroupDto.from(groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено")));
    }

    public GroupDto addUsersToGroup(Long id, List<Long> userIds) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено"));
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
            user.setGroup(group);
            users.add(user);
        }
        group.getUsers().addAll(users);

        userRepository.saveAll(users);
        groupRepository.save(group);
        return new GroupDto(group.getId(), group.getGroupName(), UserDto.fromList(group.getUsers()));
    }

    public GroupDisciplineDto addDisciplineToGroup(Long groupId, Long disciplineId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        Discipline discipline = disciplineRepository.findById(disciplineId).orElseThrow(() -> new NotFoundException("Дисциплина с таким id не найдена"));

        if (group.getDisciplines().contains(discipline)) {
            throw new ConflictException("Такая дисциплина уже добавлена");
        }

        group.getDisciplines().add(discipline);
        discipline.getGroups().add(group);
        groupRepository.save(group);

        return new GroupDisciplineDto(GroupDto.from(group), DisciplineDto.from(discipline));
    }

    public List<DisciplineDto> getDisciplinesByGroupId(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        return DisciplineDto.fromSet(group.getDisciplines());
    }
}
