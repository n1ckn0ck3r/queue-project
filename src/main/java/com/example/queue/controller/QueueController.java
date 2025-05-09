package com.example.queue.controller;

import com.example.queue.dto.ListDataDto;
import com.example.queue.dto.QueueDto;
import com.example.queue.dto.QueueUserDto;
import com.example.queue.dto.UserDto;
import com.example.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queues")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;

    @GetMapping("/")
    public ResponseEntity<List<QueueDto>> getQueues() {
        return ResponseEntity.ok(queueService.getQueues());
    }

    @PostMapping("/")
    public ResponseEntity<QueueDto> addQueue(@RequestBody QueueDto queueDto) {
        return ResponseEntity.ok(queueService.addQueue(queueDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueueDto> getQueueById(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.getQueueById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<QueueDto> updateQueueById(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(queueService.updateQueueById(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QueueDto> deleteQueueById(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.deleteQueueById(id));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDto>> getUsersByQueueId(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.getUsersByQueueId(id));
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<List<QueueUserDto>> addUsersToQueue(@PathVariable Long id, @RequestBody ListDataDto<Long> userIds) {
        return ResponseEntity.ok(queueService.addUsersToQueue(id, userIds.getData()));
    }

    @DeleteMapping("/{id}/users")
    public ResponseEntity<List<QueueUserDto>> deleteUsersFromQueue(@PathVariable Long id, @RequestBody ListDataDto<Long> userIds) {
        return ResponseEntity.ok(queueService.deleteUsersFromQueue(id, userIds.getData()));
    }

    @PostMapping("/{queueId}/users/{userId}")
    public ResponseEntity<QueueUserDto> addUserToQueue(@PathVariable Long queueId, @PathVariable Long userId) {
        return ResponseEntity.ok(queueService.addUserToQueue(queueId, userId));
    }

    @DeleteMapping("/{queueId}/users/{userId}")
    public ResponseEntity<QueueUserDto> deleteUserFromQueue(@PathVariable Long queueId, @PathVariable Long userId) {
        return ResponseEntity.ok(queueService.deleteUserFromQueue(queueId, userId));
    }
}
