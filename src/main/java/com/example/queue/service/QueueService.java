package com.example.queue.service;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.dto.QueueDto;
import com.example.queue.dto.QueueUserDto;
import com.example.queue.dto.UserDto;
import com.example.queue.exception.ConflictException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.Discipline;
import com.example.queue.model.Queue;
import com.example.queue.model.User;
import com.example.queue.repository.DisciplineRepository;
import com.example.queue.repository.QueueRepository;
import com.example.queue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QueueService {
    private final QueueRepository queueRepository;
    private final DisciplineRepository disciplineRepository;
    private final UserRepository userRepository;

    public List<QueueDto> getQueues() {
        return QueueDto.fromList(queueRepository.findAll());
    }

    public QueueDto getQueueById(Long id) {
        return QueueDto.from(queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь не найдена")));
    }

    public QueueDto addQueue(QueueDto queueDto) {
        Discipline discipline = disciplineRepository.findByDisciplineName(queueDto.getDiscipline().getDisciplineName()).orElseThrow(() -> new NotFoundException("Дисциплина не существует"));

        Queue queue = new Queue();
        queue.setActive(true);
        queue.setDiscipline(discipline);

        queueRepository.save(queue);

        return QueueDto.from(queue);
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

        return new QueueUserDto(QueueDto.from(queue), UserDto.from(user));
    }

    public List<UserDto> getUsersByQueueId(Long id) {
        Queue queue = queueRepository.findById(id).orElseThrow(() -> new NotFoundException("Очередь с таким id не найдена"));
        return UserDto.fromSet(queue.getUsers());
    }
}
