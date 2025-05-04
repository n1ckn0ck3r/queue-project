package com.example.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String disciplineName;
}
