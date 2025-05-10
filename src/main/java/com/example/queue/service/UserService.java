package com.example.queue.service;

import com.example.queue.dto.QueueDto;
import com.example.queue.dto.UserDto;
import com.example.queue.exception.BadRequestException;
import com.example.queue.exception.NotFoundException;
import com.example.queue.model.User;
import com.example.queue.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final ObjectMapper objectMapper;

   @Override
   public User loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден"));
   }

   public User loadUserById(Long id) {
       return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
   }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null) != null;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null) != null;
    }

   public UserDto getUserDtoById(Long id) {
       return UserDto.from(loadUserById(id));
   }

   public List<UserDto> getUserDtoList() {
       return UserDto.fromList(userRepository.findAll());
   }

   public UserDto updateUserById(Long id, Map<String, Object> updates) {
       User user = loadUserById(id);

       if (updates.containsKey("password")) {
           String rawPassword = (String) updates.get("password");
           String encodedPassword = passwordEncoder.encode(rawPassword);
           updates.put("password", encodedPassword);
       }

       try {
           objectMapper.updateValue(user, updates);
       } catch (JsonMappingException e) {
           throw new BadRequestException(e.getMessage());
       }

       userRepository.save(user);
       return UserDto.from(user);
   }

   public UserDto updateUserTotallyById(Long id, UserDto updates) {
       User user = loadUserById(id);
       user.setUsername(updates.getUsername());
       user.setEmail(updates.getEmail());
       user.setRole(updates.getRole());
       userRepository.save(user);
       return UserDto.from(user);
   }

    public UserDto deleteUserById(Long id) {
        User user = loadUserById(id);
        userRepository.delete(user);
        return UserDto.from(user);
    }

    public List<QueueDto> getQueuesByUserId(Long id) {
        return QueueDto.fromSet(loadUserById(id).getQueues());
    }
}
