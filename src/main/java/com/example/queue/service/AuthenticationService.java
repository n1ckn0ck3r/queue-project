package com.example.queue.service;

import com.example.queue.dto.AuthenticationResponseDto;
import com.example.queue.dto.LoginRequestDto;
import com.example.queue.dto.RegistrationRequestDto;
import com.example.queue.exception.BadRequestException;
import com.example.queue.exception.UnauthorizedException;
import com.example.queue.model.Token;
import com.example.queue.model.User;
import com.example.queue.repository.TokenRepository;
import com.example.queue.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponseDto register(RegistrationRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Пользователь с таким именем уже существует");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Пользователь с такой почтой уже существует");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    private void revokeAllToken(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokenByUser(user.getId());

        if (!validTokens.isEmpty()) {
            validTokens.forEach(t -> t.setLoggedOut(true));
        }

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()
          )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Не найдено такого пользователя"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllToken(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    public AuthenticationResponseDto refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Попытка  неавторизованного доступа");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Не найдено такого пользователя"));

        if (jwtService.isRefreshTokenValid(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllToken(user);
            saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponseDto(accessToken, refreshToken);
        }

        throw new UnauthorizedException("Попытка  неавторизованного доступа");
    }
}
