package com.github.vinicius2335.certification.utils;

import com.github.vinicius2335.certification.domain.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public abstract class StudentCreator {

    public static Student createStudent(){
        return Student.builder()
                .id(UUID.randomUUID())
                .email("teste@email.com")
                .certifications(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
