package com.github.vinicius2335.certification.api.representation.model.request;

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
    private String email;
    private String technology;
    private List<StudentQuestionAnswerRequest> studentQuestionsAnswers;
}
