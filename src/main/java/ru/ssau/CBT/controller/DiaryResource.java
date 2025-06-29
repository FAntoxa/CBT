package ru.ssau.CBT.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ssau.CBT.model.Diary;
import ru.ssau.CBT.model.DiaryDto;
import ru.ssau.CBT.model.DiaryMapper;
import ru.ssau.CBT.repository.DiaryRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/diaries")
@RequiredArgsConstructor
public class DiaryResource {

    private final DiaryRepository diaryRepository;

    private final DiaryMapper diaryMapper;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<DiaryDto> getAll(Pageable pageable) {
        Page<Diary> diaries = diaryRepository.findAll(pageable);
        Page<DiaryDto> diaryDtoPage = diaries.map(diaryMapper::toDiaryDto);
        return new PagedModel<>(diaryDtoPage);
    }

    @GetMapping("/{id}")
    public DiaryDto getOne(@PathVariable Long id) {
        Optional<Diary> diaryOptional = diaryRepository.findById(id);
        return diaryMapper.toDiaryDto(diaryOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @GetMapping("/by-ids")
    public List<DiaryDto> getMany(@RequestParam List<Long> ids) {
        List<Diary> diaries = diaryRepository.findAllById(ids);
        return diaries.stream()
                .map(diaryMapper::toDiaryDto)
                .toList();
    }

    @PostMapping
    public DiaryDto create(@RequestBody DiaryDto dto) {
        Diary diary = diaryMapper.toEntity(dto);
        Diary resultDiary = diaryRepository.save(diary);
        return diaryMapper.toDiaryDto(resultDiary);
    }

    @PatchMapping("/{id}")
    public DiaryDto patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Diary diary = diaryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        DiaryDto diaryDto = diaryMapper.toDiaryDto(diary);
        objectMapper.readerForUpdating(diaryDto).readValue(patchNode);
        diaryMapper.updateWithNull(diaryDto, diary);

        Diary resultDiary = diaryRepository.save(diary);
        return diaryMapper.toDiaryDto(resultDiary);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Diary> diaries = diaryRepository.findAllById(ids);

        for (Diary diary : diaries) {
            DiaryDto diaryDto = diaryMapper.toDiaryDto(diary);
            objectMapper.readerForUpdating(diaryDto).readValue(patchNode);
            diaryMapper.updateWithNull(diaryDto, diary);
        }

        List<Diary> resultDiaries = diaryRepository.saveAll(diaries);
        return resultDiaries.stream()
                .map(Diary::getDiary_id)
                .toList();
    }

    @DeleteMapping("/{id}")
    public DiaryDto delete(@PathVariable Long id) {
        Diary diary = diaryRepository.findById(id).orElse(null);
        if (diary != null) {
            diaryRepository.delete(diary);
        }
        return diaryMapper.toDiaryDto(diary);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        diaryRepository.deleteAllById(ids);
    }
}
