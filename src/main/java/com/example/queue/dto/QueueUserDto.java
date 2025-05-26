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
    private QueueDto queue;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDto user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime joinedAt = OffsetDateTime.now();

    public QueueUserDto(QueueDto queue, UserDto user) {
        this.queue = queue;
        this.user = user;
    }

    public QueueUserDto(QueueDto queue, UserDto user, OffsetDateTime joinedAt) {
        this.queue = queue;
        this.user = user;
        this.joinedAt = joinedAt;
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
            queueUser.getJoinedAt()
        );
    }
}
