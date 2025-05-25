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

    @PostMapping
    public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto groupDto) {
        return ResponseEntity.ok(groupService.addGroup(groupDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroupById(@PathVariable Long id, @RequestBody GroupDto groupDto) {
        return ResponseEntity.ok(groupService.updateGroupById(id, groupDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupDto> deleteGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.deleteGroupById(id));
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<GroupDto> addUsersToGroup(@PathVariable Long id, @RequestBody ListDataDto<Long> userIds) {
        return ResponseEntity.ok(groupService.addUsersToGroup(id, userIds.getData()));
    }

    @DeleteMapping("/{id}/users")
    public ResponseEntity<GroupDto> deleteUsersFromGroup(@PathVariable Long id, @RequestBody ListDataDto<Long> userIds) {
        return ResponseEntity.ok(groupService.deleteUsersFromGroup(id, userIds.getData()));
    }

    @GetMapping("/{id}/disciplines")
    public ResponseEntity<List<DisciplineDto>> getDisciplinesByGroupId(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getDisciplinesByGroupId(id));
    }

    @PostMapping("/{id}/disciplines")
    public ResponseEntity<List<GroupDisciplineDto>> addDisciplinesToGroup(@PathVariable Long id, @RequestBody ListDataDto<Long> disciplineIds) {
        return ResponseEntity.ok(groupService.addDisciplinesToGroup(id, disciplineIds.getData()));
    }

    @DeleteMapping("/{id}/disciplines")
    public ResponseEntity<List<GroupDisciplineDto>> deleteDisciplinesFromGroup(@PathVariable Long id, @RequestBody ListDataDto<Long> disciplineIds) {
        return ResponseEntity.ok(groupService.deleteDisciplinesFromGroup(id, disciplineIds.getData()));
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<GroupDto> addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.addUserToGroup(groupId, userId));
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<GroupDto> deleteUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.deleteUserFromGroup(groupId, userId));
    }

    @PostMapping("/{groupId}/disciplines/{disciplineId}")
    public ResponseEntity<GroupDisciplineDto> addDisciplineToGroup(@PathVariable Long groupId, @PathVariable Long disciplineId) {
        return ResponseEntity.ok(groupService.addDisciplineToGroup(groupId, disciplineId));
    }

    @DeleteMapping("/{groupId}/disciplines/{disciplineId}")
    public ResponseEntity<GroupDisciplineDto> deleteDisciplineFromGroup(@PathVariable Long groupId, @PathVariable Long disciplineId) {
        return ResponseEntity.ok(groupService.deleteDisciplineFromGroup(groupId, disciplineId));
    }
}
