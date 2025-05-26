package com.example.queue.repository;

import com.example.queue.model.Queue;
import com.example.queue.model.QueueUser;
import com.example.queue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueueUserRepository extends JpaRepository<QueueUser, Long> {
    void deleteByQueue(Queue queue);
    List<QueueUser> findByQueueOrderByJoinedAtAsc(Queue queue);
    Optional<QueueUser> findByQueueAndUser(Queue queue, User user);
}
