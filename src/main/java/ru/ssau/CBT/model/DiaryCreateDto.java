package ru.ssau.CBT.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryCreateDto {
    private String username;
    private LocalDate date;
    private String thought;
    private String mood;
    private Integer countnegative;
} 