package com.example.queue.controller;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.dto.GroupDisciplineDto;
import com.example.queue.dto.GroupDto;
import com.example.queue.service.GroupDisciplineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("group-disciplines")
@RequiredArgsConstructor
public class GroupDisciplineController {
    private final GroupDisciplineService groupDisciplineService;

    @PostMapping("/")
    public ResponseEntity<GroupDisciplineDto> linkGroupAndDiscipline(@RequestBody GroupDisciplineDto groupDisciplineDto) {
        return ResponseEntity.ok(groupDisciplineService.linkGroupAndDiscipline(groupDisciplineDto));
    }

    @GetMapping("/{id}/disciplines")
    public ResponseEntity<List<DisciplineDto>> getDisciplinesByGroupId(@PathVariable Long id) {
        return ResponseEntity.ok(groupDisciplineService.getDisciplinesByGroupId(id));
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<GroupDto>> getGroupsByDisciplineId(@PathVariable Long id) {
        return ResponseEntity.ok(groupDisciplineService.getGroupsByDisciplineId(id));
    }
}
