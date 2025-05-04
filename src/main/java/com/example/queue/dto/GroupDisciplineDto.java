package com.example.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDisciplineDto {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long groupId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long disciplineId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private GroupDto groupDto;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DisciplineDto disciplineDto;

    public GroupDisciplineDto(GroupDto groupDto, DisciplineDto disciplineDto) {
        this.groupDto = groupDto;
        this.disciplineDto = disciplineDto;
    }
}
