package com.example.queue.dto;

import com.example.queue.model.Queue;
import com.example.queue.model.QueueUser;
import com.example.queue.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime joinTime;

    public QueueUserDto(QueueDto queueDto, UserDto userDto) {
        this.queueDto = queueDto;
        this.userDto = userDto;
    }

    public QueueUserDto(QueueDto queueDto, UserDto userDto, OffsetDateTime joinTime) {
        this.queueDto = queueDto;
        this.userDto = userDto;
        this.joinTime = joinTime;
    }

    public static QueueUserDto from(QueueDto queueDto, UserDto userDto) {
        return new QueueUserDto(queueDto, userDto);
    }

    public static QueueUserDto from(Queue queue, User user) {
        return new QueueUserDto(QueueDto.from(queue), UserDto.from(user));
    }

    public static QueueUserDto from(QueueUser queueUser) {
        return new QueueUserDto(
            QueueDto.from(queueUser.getQueue()), 
            UserDto.from(queueUser.getUser()),
            queueUser.getJoinTime()
        );
    }
}
