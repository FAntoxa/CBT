package ru.ssau.CBT.model;

import lombok.Value;

import java.time.LocalDate;

/**
 * DTO for {@link Diary}
 */
@Value
public class DiaryDto {
    Long diary_id;
    User user;
    LocalDate date;
    String thought;
    String mood;
    int negativeCount;
}