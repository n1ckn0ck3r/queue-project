package com.example.queue.service;

import com.example.queue.dto.UserDto;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.User;
import com.example.queue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
   private final UserRepository userRepository;

   @Override
   public User loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден"));
   }

   public User loadUserById(Long id) {
       return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
   }

   public UserDto loadUserDtoById(Long id) {
       User user = loadUserById(id);
       return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
   }

   public List<UserDto> loadAllUserDtoList() {
       return UserDto.fromList(userRepository.findAll());
   }

   public boolean existsByUsername(String username) {
       User user = userRepository.findByUsername(username).orElse(null);
       return user != null;
   }

   public boolean existsByEmail(String email) {
       User user = userRepository.findByEmail(email).orElse(null);
       return user != null;
   }
}
