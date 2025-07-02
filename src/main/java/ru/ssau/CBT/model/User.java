package ru.ssau.CBT.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "passwordhash", length = 100)
    private String passwordhash;

    @OneToMany(mappedBy = "username")
    private Set<Diary> diaries = new LinkedHashSet<>();

}