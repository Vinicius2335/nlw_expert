package com.github.vinicius2335.certification.api.controller;

import com.github.vinicius2335.certification.api.representation.model.response.IQuestionAnswerResponse;
import com.github.vinicius2335.certification.domain.exception.BusinessRulesException;
import com.github.vinicius2335.certification.domain.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepository;

    /**
     * Endpoint responsável por retornar todas as questões relacionadas a uma tecnologia
     * @param technology - tecnologia que queremos encontrar suas questões
     * @return lista de perguntas com suas alternativas
     */
    @GetMapping("/technology/{technology}")
    public List<IQuestionAnswerResponse> findByTechnology(@PathVariable String technology){
        List<IQuestionAnswerResponse> listQuestionAnswerResponses = questionRepository.customFindByTechnology(technology);

        if (listQuestionAnswerResponses.isEmpty()){
            throw new BusinessRulesException("No issues found related to the requested technology");
        }

        return listQuestionAnswerResponses;
    }
}
