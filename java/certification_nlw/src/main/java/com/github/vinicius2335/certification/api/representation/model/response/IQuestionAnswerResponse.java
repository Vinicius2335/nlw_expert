package com.github.vinicius2335.certification.api.representation.model.response;

import java.util.List;
import java.util.UUID;

/**
 * DTO que retorna uma pergunta e suas alternativas
 */
public interface IQuestionAnswerResponse {
    UUID getId();
    String getTechnology();
    String getDescription();
    List<IAlternativeResponse> getAlternatives();
}
