package com.example.queue.dto;

import com.example.queue.model.Discipline;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String disciplineName;

    public static DisciplineDto from(Discipline discipline) {
        return new DisciplineDto(discipline.getId(), discipline.getDisciplineName());
    }

    public static List<DisciplineDto> fromSet(Set<Discipline> disciplines) {
        return disciplines.stream().map(DisciplineDto::from).collect(Collectors.toList());
    }
}
