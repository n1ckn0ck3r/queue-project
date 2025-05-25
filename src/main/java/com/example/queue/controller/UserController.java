package com.example.queue.controller;

import com.example.queue.dto.QueueDto;
import com.example.queue.dto.UserDto;
import com.example.queue.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUserDtoList() {
        return ResponseEntity.ok(userService.getUserDtoList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDtoById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDtoById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(userService.updateUserById(id, updates));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserTotallyById(@PathVariable Long id, @RequestBody UserDto updates) {
        return ResponseEntity.ok(userService.updateUserTotallyById(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @GetMapping("/{id}/queues")
    public ResponseEntity<List<QueueDto>> getQueuesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getQueuesByUserId(id));
    }
}
