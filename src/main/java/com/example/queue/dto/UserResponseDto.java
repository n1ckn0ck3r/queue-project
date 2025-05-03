package com.example.queue.dto;

import com.example.queue.model.Role;

public record UserResponseDto (Long id, String name, String email, Role role) { }
