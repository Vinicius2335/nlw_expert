package com.github.vinicius2335.certification.utils;

import com.github.vinicius2335.certification.domain.model.Question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public abstract class QuestionCreator {
    public static Question createQuestion(){
        return Question.builder()
                .id(UUID.randomUUID())
                .technology("JAVA")
                .description("Teste")
                .createdAt(LocalDateTime.now())
                .alternatives(new ArrayList<>())
                .build();
    }
}
