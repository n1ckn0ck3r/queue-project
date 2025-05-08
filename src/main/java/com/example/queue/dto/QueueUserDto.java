package com.example.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueUserDto {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long queueId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private QueueDto queueDto;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDto userDto;

    public QueueUserDto(QueueDto queueDto, UserDto userDto) {
        this.queueDto = queueDto;
        this.userDto = userDto;
    }
}
