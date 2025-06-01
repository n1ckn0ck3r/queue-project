package com.example.queue.dto;

import com.example.queue.model.Role;
import com.example.queue.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String username;
    private String email;
    private Role role;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime joinedAt;

    public UserDto(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public static UserDto from(User user, OffsetDateTime joinedAt) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), joinedAt);
    }

    public static List<UserDto> fromList(List<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }

    public static List<UserDto> fromSet(Set<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }
}
