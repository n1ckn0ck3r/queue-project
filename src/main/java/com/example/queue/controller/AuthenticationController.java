package com.example.queue.controller;

import com.example.queue.dto.AuthenticationResponseDto;
import com.example.queue.dto.LoginRequestDto;
import com.example.queue.dto.RegistrationRequestDto;
import com.example.queue.service.AuthenticationService;
import com.example.queue.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        if (userService.existsByUsername(registrationRequestDto.getUsername())) {
            return ResponseEntity.badRequest().body("Имя занято");
        }

        if (userService.existsByEmail(registrationRequestDto.getEmail())) {
            return ResponseEntity.badRequest().body("Почта занята");
        }

        authenticationService.register(registrationRequestDto);

        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authenticationService.refreshToken(request, response);
    }
}
