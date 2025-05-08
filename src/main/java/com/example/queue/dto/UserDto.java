package com.example.queue.dto;

import com.example.queue.model.Role;
import com.example.queue.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public static List<UserDto> fromList(List<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }

    public static List<UserDto> fromSet(Set<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }
}
