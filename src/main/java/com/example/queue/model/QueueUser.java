package com.example.queue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "queue_users")
public class QueueUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Long id;

    @ManyToOne @JoinColumn(name = "queue_id") @ToString.Exclude @EqualsAndHashCode.Exclude
    private Queue queue;

    @ManyToOne @JoinColumn(name = "user_id") @ToString.Exclude @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "joined_at")
    private OffsetDateTime joinedAt;

    public QueueUser(Queue queue, User user) {
        this.queue = queue;
        this.user = user;
    }

    public QueueUser(Queue queue, User user, OffsetDateTime joinedAt) {
        this.queue = queue;
        this.user = user;
        this.joinedAt = joinedAt;
    }
}
