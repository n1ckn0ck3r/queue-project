package com.example.queue.controller;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("disciplines")
@RequiredArgsConstructor
public class DisciplineController {
    private final DisciplineService disciplineService;

    @PostMapping
    public ResponseEntity<DisciplineDto> addDiscipline(@RequestBody DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineService.addDiscipline(disciplineDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineDto> getDisciplineById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplineService.getDisciplineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineDto> updateDisciplineById(@PathVariable Long id, @RequestBody DisciplineDto updates) {
        return ResponseEntity.ok(disciplineService.updateDisciplineById(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisciplineDto> deleteDisciplineById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplineService.deleteDisciplineById(id));
    }
}
