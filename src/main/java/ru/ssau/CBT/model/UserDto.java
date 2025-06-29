package ru.ssau.CBT.model;

import lombok.Value;

import java.util.Set;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto {
    String username;
    String passwordHash;
    Set<DiaryDto> diaries;
}