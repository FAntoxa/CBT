package ru.ssau.CBT.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.CBT.model.DiaryDto;
import ru.ssau.CBT.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryDto> createEntry(@RequestBody DiaryDto createDto) {
        DiaryDto created = diaryService.createDiaryEntry(createDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{username}/{date}")
    public ResponseEntity<DiaryDto> getEntry(
            @PathVariable("username") String username,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DiaryDto entry = diaryService.getDiaryEntry(username, date);
        if (entry != null) {
            return ResponseEntity.ok(entry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}/{date}/previous")
    public ResponseEntity<DiaryDto> getPreviousEntry(
            @PathVariable("username") String username,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DiaryDto entry = diaryService.getPreviousEntry(username, date);
        if (entry != null) {
            return ResponseEntity.ok(entry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}/{date}/next")
    public ResponseEntity<DiaryDto> getNextEntry(
            @PathVariable("username") String username,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DiaryDto entry = diaryService.getNextEntry(username, date);
        if (entry != null) {
            return ResponseEntity.ok(entry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<DiaryDto>> getAllEntries(@PathVariable("username") String username) {
        List<DiaryDto> entries = diaryService.getAllEntries(username);
        return ResponseEntity.ok(entries);
    }
} 