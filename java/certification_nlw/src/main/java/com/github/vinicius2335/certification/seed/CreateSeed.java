package com.github.vinicius2335.certification.seed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.*;

public class CreateSeed {
    private final JdbcTemplate jdbcTemplate;

    public CreateSeed(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/db_nlw_expert");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        CreateSeed createSeed = new CreateSeed(dataSource);
        createSeed.run();
    }

    public void run(){
        executeSqlFile("src/main/resources/create.sql");
    }

    public void executeSqlFile(String filePath){
        try {
            String sqlScript = new String(Files.readAllBytes(Paths.get(filePath)));
            jdbcTemplate.execute(sqlScript);

            out.println("Seed realizado com sucesso...");
        } catch (IOException e) {
            err.println("Erro ao executar arquivo " + e.getMessage());
        }
    }
}
