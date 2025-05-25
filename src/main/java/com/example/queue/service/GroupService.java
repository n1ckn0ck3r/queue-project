package com.example.queue.service;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.dto.GroupDisciplineDto;
import com.example.queue.dto.GroupDto;
import com.example.queue.exception.BadRequestException;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.model.Group;
import com.example.queue.model.User;
import com.example.queue.repository.DisciplineRepository;
import com.example.queue.repository.GroupRepository;
import com.example.queue.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final DisciplineRepository disciplineRepository;
    private final ObjectMapper objectMapper;

    public List<GroupDto> getGroups() {
        return GroupDto.fromList(groupRepository.findAll());
    }

    public GroupDto addGroup(GroupDto groupDto) {
        if (groupRepository.findByGroupName(groupDto.getGroupName()).isPresent()) {
            throw new ConflictException("Группа с таким названием уже существует");
        }

        Group group = new Group(groupDto.getGroupName());
        groupRepository.save(group);

        return GroupDto.from(group);
    }

    public GroupDto getGroupById(Long id) {
        return GroupDto.from(groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено")));
    }

    public GroupDto updateGroupById(Long id, Map<String, Object> updates) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));

        try {
            objectMapper.updateValue(group, updates);
        } catch (JsonMappingException e) {
            throw new BadRequestException(e.getMessage());
        }

        groupRepository.save(group);
        return GroupDto.from(group);
    }

    public GroupDto updateGroupTotallyById(Long id, GroupDto groupDto) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        group.setGroupName(groupDto.getGroupName());
        groupRepository.save(group);
        return GroupDto.from(group);
    }

    public GroupDto deleteGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        groupRepository.delete(group);
        return GroupDto.from(group);
    }

    public List<DisciplineDto> getDisciplinesByGroupId(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        return DisciplineDto.fromSet(group.getDisciplines());
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
        return GroupDto.from(group);
    }

    public GroupDto deleteUsersFromGroup(Long id, List<Long> userIds) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
            user.setGroup(null);
            users.add(user);
        }
        group.getUsers().removeAll(users);

        userRepository.saveAll(users);
        return GroupDto.from(group);
    }

    public List<GroupDisciplineDto> addDisciplinesToGroup(Long groupId, List<Long> disciplineIds) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        List<GroupDisciplineDto> dtoList = new ArrayList<>();
        for (Long disciplineId : disciplineIds) {
            Discipline discipline = disciplineRepository.findById(disciplineId).orElseThrow(() -> new NotFoundException("Дисциплина с id " + disciplineId + " не найдена"));
            if (group.getDisciplines().contains(discipline)) {
                throw new ConflictException("У этой группы уже есть дисциплина c id " + disciplineId);
            }

            discipline.getGroups().add(group);
            group.getDisciplines().add(discipline);
            dtoList.add(GroupDisciplineDto.from(group, discipline));
        }

        groupRepository.save(group);
        return dtoList;
    }

    public List<GroupDisciplineDto> deleteDisciplinesFromGroup(Long groupId, List<Long> disciplineIds) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        List<GroupDisciplineDto> dtoList = new ArrayList<>();
        for (Long disciplineId : disciplineIds) {
            Discipline discipline = disciplineRepository.findById(disciplineId).orElseThrow(() -> new NotFoundException("Дисциплина с id " + disciplineId + " не найдена"));
            if (!group.getDisciplines().contains(discipline)) {
                throw new BadRequestException("Нечего удалять: у этой группы нет дисциплины c id " + disciplineId);
            }

            discipline.getGroups().remove(group);
            group.getDisciplines().remove(discipline);
            dtoList.add(GroupDisciplineDto.from(group, discipline));
        }

        groupRepository.save(group);
        return dtoList;
    }

    public GroupDto addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));

        if (group.getUsers().contains(user)) {
            throw new ConflictException("Этот пользователь уже добавлен в группу");
        }

        group.getUsers().add(user);
        user.setGroup(group);
        userRepository.save(user);

        return GroupDto.from(group);
    }

    public GroupDto deleteUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));

        if (!group.getUsers().contains(user)) {
            throw new BadRequestException("Этого пользователя и так нет в группе");
        }

        group.getUsers().remove(user);
        user.setGroup(null);
        userRepository.save(user);

        return GroupDto.from(group);
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

        return GroupDisciplineDto.from(group, discipline);
    }

    public GroupDisciplineDto deleteDisciplineFromGroup(Long groupId, Long disciplineId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группа с таким id не найдена"));
        Discipline discipline = disciplineRepository.findById(disciplineId).orElseThrow(() -> new NotFoundException("Дисциплина с таким id не найдена"));

        if (!group.getDisciplines().contains(discipline)) {
            throw new BadRequestException("Нечего удалять: у этой группы нет такой дисциплины");
        }

        group.getDisciplines().remove(discipline);
        discipline.getGroups().remove(group);
        groupRepository.save(group);

        return GroupDisciplineDto.from(group, discipline);
    }
}
