package ru.ssau.CBT.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Users")
public class User {

    @Id
    @Column(name = "username")
    private  String  username;

    @Column(name = "password")
    private String passwordHash;

    @OneToMany(mappedBy = "username")
    private Set<Diary> diaries = new LinkedHashSet<>();

}
