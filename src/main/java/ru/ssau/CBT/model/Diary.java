package ru.ssau.CBT.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User username;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "thought")
    private String thought;

    @Column(name = "mood")
    private String mood;

    @Column(name = "countnegative")
    private Integer countnegative;

}