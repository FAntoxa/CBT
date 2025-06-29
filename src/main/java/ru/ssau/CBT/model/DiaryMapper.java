package ru.ssau.CBT.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DiaryMapper {
    Diary toEntity(DiaryDto diaryDto);

    DiaryDto toDiaryDto(Diary diary);

    Diary updateWithNull(DiaryDto diaryDto, @MappingTarget Diary diary);
}