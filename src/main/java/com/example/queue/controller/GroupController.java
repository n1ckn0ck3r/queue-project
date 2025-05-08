package com.example.queue.controller;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.dto.GroupDisciplineDto;
import com.example.queue.dto.GroupDto;
import com.example.queue.dto.ListDataDto;
import com.example.queue.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/add")
    public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto groupDto) {
        return ResponseEntity.ok(groupService.addGroup(groupDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<GroupDto> addUsersToGroup(@PathVariable Long id, @RequestBody ListDataDto<Long> userIds) {
        return ResponseEntity.ok(groupService.addUsersToGroup(id, userIds.getData()));
    }

    @PostMapping("/{groupId}/disciplines/{disciplineId}")
    public ResponseEntity<GroupDisciplineDto> addDisciplineToGroup(@PathVariable Long groupId, @PathVariable Long disciplineId) {
        return ResponseEntity.ok(groupService.addDisciplineToGroup(groupId, disciplineId));
    }

    @GetMapping("/{id}/disciplines")
    public ResponseEntity<List<DisciplineDto>> getDisciplinesByGroupId(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getDisciplinesByGroupId(id));
    }
}
