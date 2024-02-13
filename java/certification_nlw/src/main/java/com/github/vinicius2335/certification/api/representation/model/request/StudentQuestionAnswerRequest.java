package com.github.vinicius2335.certification.api.representation.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "QuestionID cannot be null or empty")
    private UUID questionId;

    @NotBlank(message = "AlternativeID cannot be null or empty")
    private UUID alternativeId;

    @NotNull(message = "isCorrect cannot be null")
    private boolean isCorrect;
}
