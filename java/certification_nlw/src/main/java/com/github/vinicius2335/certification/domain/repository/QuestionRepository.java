package com.github.vinicius2335.certification.domain.repository;

import com.github.vinicius2335.certification.api.representation.model.response.IQuestionAnswerResponse;
import com.github.vinicius2335.certification.domain.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // Usando Projeção JPA para retornar um response mais customizado com apenas as informações necessárias
    @Query("SELECT q FROM Question q WHERE q.technology = :technology")
    List<IQuestionAnswerResponse> customFindByTechnology(String technology);

    List<Question> findByTechnology(String technology);
}
