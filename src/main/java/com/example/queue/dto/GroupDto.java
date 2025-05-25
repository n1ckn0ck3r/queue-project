package com.example.queue.dto;

import com.example.queue.model.Group;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String groupName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UserDto> users = new ArrayList<>();

    public static GroupDto from(Group group) {
        return new GroupDto(group.getId(), group.getGroupName(), UserDto.fromList(group.getUsers()));
    }

    public static List<GroupDto> fromList(List<Group> groups) {
        return groups.stream().map(GroupDto::from).collect(Collectors.toList());
    }

    public static List<GroupDto> fromSet(Set<Group> groups) {
        return groups.stream().map(GroupDto::from).collect(Collectors.toList());
    }
}
