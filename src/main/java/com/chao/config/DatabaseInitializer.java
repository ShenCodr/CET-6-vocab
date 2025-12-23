package com.chao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Create table if not exists
        String createTableSql = "CREATE TABLE IF NOT EXISTS tb_user (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        jdbcTemplate.execute(createTableSql);

        // Insert default user if not exists
        String checkUserSql = "SELECT COUNT(*) FROM tb_user WHERE username = 'chao'";
        Integer count = jdbcTemplate.queryForObject(checkUserSql, Integer.class);
        
        if (count != null && count == 0) {
            String insertUserSql = "INSERT INTO tb_user (username, password) VALUES ('chao', '123321')";
            jdbcTemplate.execute(insertUserSql);
            System.out.println("Default user 'chao' created.");
        }

        // Fix: Assign orphan words (user_id is null) to default user 'chao'
        // Get chao's ID
        try {
            Integer chaoId = jdbcTemplate.queryForObject("SELECT id FROM tb_user WHERE username = 'chao'", Integer.class);
            if (chaoId != null) {
                String updateWordsSql = "UPDATE tb_word SET user_id = ? WHERE user_id IS NULL";
                int updatedRows = jdbcTemplate.update(updateWordsSql, chaoId);
                if (updatedRows > 0) {
                    System.out.println("Assigned " + updatedRows + " orphan words to user 'chao'.");
                }
            }
        } catch (Exception e) {
            System.out.println("Auto-assignment of words skipped: " + e.getMessage());
        }
    }
}
