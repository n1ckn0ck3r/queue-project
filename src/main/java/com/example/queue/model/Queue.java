package com.example.queue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "queues")
public class Queue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Setter @Column(name = "id")
    private Long id;

    @ManyToOne @JoinColumn(name = "discipline_id") @ToString.Exclude @EqualsAndHashCode.Exclude
    private Discipline discipline;

    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<QueueUser> queueUsers = new ArrayList<>();

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "queue_start")
    private OffsetDateTime queueStart;

    @Column(name = "queue_end")
    private OffsetDateTime queueEnd;

    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        for (QueueUser queueUser : queueUsers) {
            users.add(queueUser.getUser());
        }
        return users;
    }

    public void addUser(User user) {
        QueueUser queueUser = new QueueUser(this, user);
        queueUsers.add(queueUser);
    }

    public void removeUser(User user) {
        queueUsers.removeIf(queueUser -> queueUser.getUser().equals(user));
    }
}
