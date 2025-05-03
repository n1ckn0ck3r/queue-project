package com.example.queue.service;

import com.example.queue.model.User;
import com.example.queue.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
   private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
       this.userRepository = userRepository;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден"));
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
