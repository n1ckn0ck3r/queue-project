package com.example.queue.service;

import com.example.queue.dto.GroupDto;
import com.example.queue.dto.UserDto;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Group;
import com.example.queue.model.User;
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

    public GroupDto addGroup(GroupDto groupDto) {
        if (groupRepository.findByGroupName(groupDto.getGroupName()).isPresent()) {
            throw new ConflictException("Группа с таким названием уже существует");
        }

        Group group = new Group(groupDto.getGroupName());
        groupRepository.save(group);

        return new GroupDto(group.getId(), group.getGroupName(), UserDto.fromList(group.getUsers()));
    }

    public GroupDto getGroupById(long id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено"));
        return new GroupDto(group.getId(), group.getGroupName(), UserDto.fromList(group.getUsers()));
    }

    public GroupDto addUsersToGroup(long id, List<Long> userIds) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группы с таким id не найдено"));
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
            user.setGroup(group);
        }
        group.getUsers().addAll(users);

        userRepository.saveAll(users);
        groupRepository.save(group);
        return new GroupDto(group.getId(), group.getGroupName(), UserDto.fromList(group.getUsers()));
    }
}
