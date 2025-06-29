package ru.ssau.CBT.model;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {DiaryMapper.class})
public interface UserMapper {
    User toEntity(UserDto userDto);

    @AfterMapping
    default void linkDiaries(@MappingTarget User user) {
        user.getDiaries().forEach(diary -> diary.setUsername(user));
    }

    UserDto toUserDto(User user);

    User updateWithNull(UserDto userDto, @MappingTarget User user);
}