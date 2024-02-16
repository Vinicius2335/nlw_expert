package com.github.vinicius2335.certification.utils;

import com.github.vinicius2335.certification.domain.model.Alternative;
import com.github.vinicius2335.certification.domain.model.Question;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AlternativeCreator {
    public static Alternative createAlternative(Question question){
        return Alternative.builder()
                .id(UUID.randomUUID())
                .question(question)
                .isCorrect(true)
                .description("Teste")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Alternative createAlternativeIncorrect(Question question){
        return Alternative.builder()
                .id(UUID.randomUUID())
                .question(question)
                .isCorrect(false)
                .description("Teste")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
