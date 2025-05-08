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
@Table(name = "groups")
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @ManyToMany @JoinTable(name = "group_disciplines",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")) @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Discipline> disciplines =  new HashSet<>();

    @OneToMany(mappedBy = "group")
    private List<User> users = new ArrayList<>();

    public Group(String groupName) {
        this.groupName = groupName;
    }
}
