package com.example.queue.controller;

import com.example.queue.dto.GroupDto;
import com.example.queue.dto.ListDataDto;
import com.example.queue.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GroupDto> getGroupById(@PathVariable long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping("/{id}/add_users")
    public ResponseEntity<GroupDto> addUsersToGroup(@PathVariable long id, @RequestBody ListDataDto<Long> userIds) {
        return ResponseEntity.ok(groupService.addUsersToGroup(id, userIds.getData()));
    }
}
