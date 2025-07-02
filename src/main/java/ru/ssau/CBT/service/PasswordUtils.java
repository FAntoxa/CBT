package ru.ssau.CBT.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtils {
    private final JdbcTemplate jdbcTemplate;
    
    public String hashPassword(String password) {
        String sql = "SELECT crypt(?, gen_salt('md5'))";
        return jdbcTemplate.queryForObject(sql, String.class, password);
    }
    
    public boolean checkPassword(String password, String storedHash) {
        String sql = "SELECT crypt(?, ?) = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, password, storedHash, storedHash));
    }
} 