package ru.ssau.CBT.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ssau.CBT.model.*;
import ru.ssau.CBT.repository.DiaryRepository;
import ru.ssau.CBT.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public DiaryDto createDiaryEntry(DiaryCreateDto createDto) {
        User user = userRepository.findById(createDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Diary diary = new Diary();
        diary.setUsername(user);
        diary.setDate(createDto.getDate());
        diary.setThought(createDto.getThought());
        diary.setMood(createDto.getMood());
        System.out.println("countnegative from DTO = " + createDto.getCountnegative());
        diary.setCountnegative(createDto.getCountnegative());

        Diary savedDiary = diaryRepository.save(diary);

        return convertToDto(savedDiary);
    }

    public DiaryDto getDiaryEntry(String username, LocalDate date) {
        Optional<Diary> diary = diaryRepository.findByUsernameAndDate(username, date);
        return diary.map(this::convertToDto).orElse(null);
    }

    public DiaryDto getPreviousEntry(String username, LocalDate currentDate) {
        Optional<Diary> previousDiary = diaryRepository.findFirstByUsernameUsernameAndDateLessThanOrderByDateDesc(username, currentDate);
        return previousDiary.map(this::convertToDto).orElse(null);
    }

    public DiaryDto getNextEntry(String username, LocalDate currentDate) {
        Optional<Diary> nextDiary = diaryRepository.findFirstByUsernameUsernameAndDateGreaterThanOrderByDateAsc(username, currentDate);
        return nextDiary.map(this::convertToDto).orElse(null);
    }

    public List<DiaryDto> getAllEntries(String username) {
        List<Diary> diaries = diaryRepository.findByUsernameOrderByDateDesc(username);
        return diaries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DiaryDto convertToDto(Diary diary) {
        return new DiaryDto(
                diary.getUsername() != null ? diary.getUsername().getUsername() : null,
                diary.getDate(),
                diary.getThought(),
                diary.getMood(),
                diary.getCountnegative()
        );
    }
}
