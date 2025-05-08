package com.example.queue.dto;

import com.example.queue.model.Queue;
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
public class QueueDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private DisciplineDto discipline;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UserDto> users = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean active;

    public static QueueDto from(Queue queue) {
        return new QueueDto(queue.getId(), DisciplineDto.from(queue.getDiscipline()), UserDto.fromSet(queue.getUsers()), queue.getActive());
    }

    public static List<QueueDto> fromList(List<Queue> queues) {
        return queues.stream().map(QueueDto::from).collect(Collectors.toList());
    }

    public static List<QueueDto> fromSet(Set<Queue> queues) {
        return queues.stream().map(QueueDto::from).collect(Collectors.toList());
    }
}
