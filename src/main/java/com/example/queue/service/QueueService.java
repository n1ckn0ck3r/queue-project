package com.example.queue.service;

import com.example.queue.dto.QueueDto;
import com.example.queue.dto.QueueUserDto;
import com.example.queue.dto.UserDto;
import com.example.queue.exception.BadRequestException;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.model.Queue;
import com.example.queue.model.User;
import com.example.queue.repository.DisciplineRepository;
import com.example.queue.repository.QueueRepository;
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
public class QueueService {
    private final QueueRepository queueRepository;
    private final DisciplineRepository disciplineRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public List<QueueDto> getQueues() {
        return QueueDto.fromList(queueRepository.findAll());
    }

    public QueueDto addQueue(QueueDto queueDto) {
        Discipline discipline = disciplineRepository.findByDisciplineName(queueDto.getDiscipline().getDisciplineName()).orElseThrow(() -> new NotFoundException("Дисциплина не существует"));

        Queue queue = new Queue();
        queue.setActive(true);
        queue.setDiscipline(discipline);
        queue.setQueueEnd(queueDto.getQueueEnd());

        queueRepository.save(queue);

        return QueueDto.from(queue);
    }

    public QueueDto getQueueById(Long id) {
        return QueueDto.from(queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь не найдена")));
    }

    public QueueDto updateQueueById(Long id, Map<String, Object> updates) {
        Queue queue = queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        try {
            objectMapper.updateValue(queue, updates);
        } catch (JsonMappingException e) {
            throw new BadRequestException(e.getMessage());
        }
        queueRepository.save(queue);
        return QueueDto.from(queue);
    }

    public QueueDto deleteQueueById(Long id) {
        Queue queue = queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        queueRepository.delete(queue);
        return QueueDto.from(queue);
    }

    public List<UserDto> getUsersByQueueId(Long id) {
        Queue queue = queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        return UserDto.fromSet(queue.getUsers());
    }

    public List<QueueUserDto> addUsersToQueue(Long id, List<Long> userIds) {
        Queue queue = queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        List<QueueUserDto> dtoList = new ArrayList<>();
        for (Long userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
            if (queue.getUsers().contains(user)) {
                throw new ConflictException("Этот пользователь уже добавлен в очередь");
            }
            user.getQueues().add(queue);
            queue.getUsers().add(user);
            dtoList.add(QueueUserDto.from(queue, user));
        }

        queueRepository.save(queue);
        return dtoList;
    }

    public List<QueueUserDto> deleteUsersFromQueue(Long id, List<Long> userIds) {
        Queue queue = queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        List<QueueUserDto> dtoList = new ArrayList<>();
        for (Long userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
            if (!queue.getUsers().contains(user)) {
                throw new BadRequestException("Этого пользователя и так нет в очереди");
            }
            user.getQueues().remove(queue);
            queue.getUsers().remove(user);
            dtoList.add(QueueUserDto.from(queue, user));
        }

        queueRepository.save(queue);
        return dtoList;
    }

    public QueueUserDto addUserToQueue(Long queueId, Long userId) {
        Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));

        if (queue.getUsers().contains(user)) {
            throw new ConflictException("Такой пользователь уже добавлен");
        }

        queue.getUsers().add(user);
        user.getQueues().add(queue);
        queueRepository.save(queue);

        return QueueUserDto.from(queue, user);
    }

    public QueueUserDto deleteUserFromQueue(Long queueId, Long userId) {
        Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));

        queue.getUsers().remove(user);
        user.getQueues().remove(queue);
        queueRepository.save(queue);

        return QueueUserDto.from(queue, user);
    }
}
