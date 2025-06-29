package ru.ssau.CBT.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Diary")
public class Diary {
    @Id
    @Column(name = "diary_id")
    private Long diary_id;

    @Column(name = "user")
    private User user;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "thought")
    private String thought;

    @Column(name = "mood")
    private String mood;

    @Column(name = "negativeCount")
    private int negativeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User username;

}
