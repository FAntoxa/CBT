package ru.ssau.CBT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.CBT.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}