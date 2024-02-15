package com.github.vinicius2335.certification.domain.repository;

import com.github.vinicius2335.certification.api.representation.model.response.IQuestionAnswerResponse;
import com.github.vinicius2335.certification.domain.model.Question;
import com.github.vinicius2335.certification.utils.QuestionCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository underTest;

    private Question question;

    @BeforeEach
    void setUp() {
        question = QuestionCreator.createQuestion();
    }

    private Question saveAndReturnNewQuestion() {
        return underTest.saveAndFlush(question);
    }

    @Test
    @DisplayName("findByTechnology() return a list of question related by tecnology")
    void givenTechnology_whenFindByTechnology_thenReturnListQuestion() {
        //given
        Question questionSaved = saveAndReturnNewQuestion();
        String technology = questionSaved.getTechnology();
        //when
        List<Question> expected = underTest.findByTechnology(technology);
        //then
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(questionSaved);
    }

    @Test
    @DisplayName("findByTechnology() return a empty list of question when tecnology not found")
    void givenTechnology_whenFindByTechnology_thenReturnEmptyListQuestion() {
        //given
        String technology = "JAVA";
        //when
        List<Question> expected = underTest.findByTechnology(technology);
        //then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("customFindByTechnology() return list of QuestionResponse related by technology")
    void givenTechnology_whenCustomFindByTechnology_thenReturnListIQuestionAnswerResponse() {
        //given
        Question questionSaved = saveAndReturnNewQuestion();
        String technology = questionSaved.getTechnology();
        //when
        List<IQuestionAnswerResponse> expected = underTest.customFindByTechnology(technology);
        //then
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .first()
                .matches(q -> q.getId().equals(questionSaved.getId()));
    }
}