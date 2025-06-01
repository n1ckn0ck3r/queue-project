package com.example.queue.controller;

import com.example.queue.dto.DisciplineDto;
import com.example.queue.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("disciplines")
@RequiredArgsConstructor
public class DisciplineController {
    private final DisciplineService disciplineService;

    @GetMapping
    public ResponseEntity<List<DisciplineDto>> getDisciplines() {
        return ResponseEntity.ok(disciplineService.getDisciplines());
    }

    @PostMapping
    public ResponseEntity<DisciplineDto> addDiscipline(@RequestBody DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineService.addDiscipline(disciplineDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineDto> getDisciplineById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplineService.getDisciplineById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DisciplineDto> updateDisciplineById(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(disciplineService.updateDisciplineById(id, updates));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineDto> updateDisciplineTotallyById(@PathVariable Long id, @RequestBody DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineService.updateDisciplineTotallyById(id, disciplineDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisciplineDto> deleteDisciplineById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplineService.deleteDisciplineById(id));
    }
}
