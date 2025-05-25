package com.example.queue.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(LocalDateTime timestamp, String message) { }
