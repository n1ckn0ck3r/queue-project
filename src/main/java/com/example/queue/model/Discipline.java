package com.example.queue.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disciplines")
public class Discipline {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Long id;

    @Column(name = "discipline_name")
    private String disciplineName;

    @ManyToMany(mappedBy = "disciplines") @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "discipline") @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Queue> queues = new ArrayList<>();

    public Discipline(String disciplineName) {
        this.disciplineName = disciplineName;
    }
}
