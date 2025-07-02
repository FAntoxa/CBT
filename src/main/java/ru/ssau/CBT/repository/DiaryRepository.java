package ru.ssau.CBT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssau.CBT.model.Diary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    
    @Query("SELECT d FROM Diary d WHERE d.username.username = :username AND d.date = :date")
    Optional<Diary> findByUsernameAndDate(@Param("username") String username, @Param("date") LocalDate date);
    
    @Query("SELECT d FROM Diary d WHERE d.username.username = :username ORDER BY d.date DESC")
    List<Diary> findByUsernameOrderByDateDesc(@Param("username") String username);
    
    Optional<Diary> findFirstByUsernameUsernameAndDateLessThanOrderByDateDesc(String username, LocalDate date);
    
    Optional<Diary> findFirstByUsernameUsernameAndDateGreaterThanOrderByDateAsc(String username, LocalDate date);
}