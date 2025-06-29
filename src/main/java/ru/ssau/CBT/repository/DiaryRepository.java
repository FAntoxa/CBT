package ru.ssau.CBT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.CBT.model.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}