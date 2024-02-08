package com.github.vinicius2335.certification.api.representation.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO que representa a pergunta e a resposta que o estudante escolheu
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentQuestionAnswerRequest {
    private UUID questionId;
    private UUID alternativeId;
    private boolean isCorrect;
}
