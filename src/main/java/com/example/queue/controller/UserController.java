package com.example.queue.controller;

import com.example.queue.dto.QueueDto;
import com.example.queue.dto.UserDto;
import com.example.queue.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.loadUserDtoById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.loadAllUserDtoList());
    }

    @GetMapping("/{id}/queues")
    public ResponseEntity<List<QueueDto>> getQueuesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getQueuesByUserId(id));
    }
}
