package com.example.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String groupName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UserDto> users;
}
