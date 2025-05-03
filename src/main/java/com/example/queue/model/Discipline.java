package com.example.queue.model;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToMany(mappedBy = "disciplines")
    private Set<Group> groups;
}
