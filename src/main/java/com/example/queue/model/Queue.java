package com.example.queue.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "queues")
public class Queue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Setter @Column(name = "id")
    private Long id;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne @JoinColumn(name = "discipline_id") @ToString.Exclude @EqualsAndHashCode.Exclude
    private Discipline discipline;

    @ManyToMany @JoinTable(
            name = "queue_users",
            joinColumns = @JoinColumn(name = "queue_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")) @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<User> users;
}
