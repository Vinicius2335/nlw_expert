package com.github.vinicius2335.certification.api.representation.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa a certificação respondida por um estudante
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentCertificationAnswerRequest {
    @Email(message = "Email cannot be invalid")
    @NotBlank(message = "Email cannot be null or empty ")
    private String email;

    @NotBlank(message = "Technology cannot be null or empty")
    private String technology;

    @NotEmpty(message = "StudentQuestionsAnswers List cannot be empty")
    private List<StudentQuestionAnswerRequest> studentQuestionsAnswers;
}
