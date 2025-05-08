package com.example.queue.controller;

import com.example.queue.dto.QueueDto;
import com.example.queue.dto.QueueUserDto;
import com.example.queue.dto.UserDto;
import com.example.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/queues")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;

    @GetMapping("/")
    public ResponseEntity<List<QueueDto>> getQueues() {
        return ResponseEntity.ok(queueService.getQueues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueueDto> getQueueById(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.getQueueById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<QueueDto> addQueue(@RequestBody QueueDto queueDto) {
        return ResponseEntity.ok(queueService.addQueue(queueDto));
    }

    @PostMapping("/{queueId}/users/{userId}")
    public ResponseEntity<QueueUserDto> addUserToQueue(@PathVariable Long queueId, @PathVariable Long userId) {
        return ResponseEntity.ok(queueService.addUserToQueue(queueId, userId));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDto>> getUsersByQueueId(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.getUsersByQueueId(id));
    }
}
