package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.api.representation.model.request.StudentCertificationAnswerRequest;
import com.github.vinicius2335.certification.api.representation.model.request.StudentQuestionAnswerRequest;
import com.github.vinicius2335.certification.api.representation.model.request.StudentVerifyHasCertification;
import com.github.vinicius2335.certification.domain.exception.BusinessRulesException;
import com.github.vinicius2335.certification.domain.exception.StudentNotFoundException;
import com.github.vinicius2335.certification.domain.model.Alternative;
import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Question;
import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import com.github.vinicius2335.certification.domain.repository.QuestionRepository;
import com.github.vinicius2335.certification.domain.repository.StudentRepository;
import com.github.vinicius2335.certification.utils.AlternativeCreator;
import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.QuestionCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CertificationAnswersServiceTest {
    @InjectMocks
    private CertificationAnswersService underTest;

    @Mock
    private StudentRepository mockStudentRepository;
    @Mock
    private QuestionRepository mockQuestionRepository;
    @Mock
    private CertificationRepository mockCertificationRepository;
    @Mock
    private VerifyIfHasCertificationService mockVerifyIfHasCertificationService;

    private Certification certification;
    private Question question;
    private Alternative alternative;

    @BeforeEach
    void setUp() {
        Student student = StudentCreator.createStudent();
        question = QuestionCreator.createQuestion();
        alternative = AlternativeCreator.createAlternative(question);

        List<Alternative> alternatives = List.of(
                alternative
        );

        question.setAlternatives(alternatives);
        certification = CertificationCreator.createCertification(student);

        BDDMockito.lenient().when(mockStudentRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(student));

        BDDMockito.lenient()
                .when(mockVerifyIfHasCertificationService.execute(ArgumentMatchers.any(StudentVerifyHasCertification.class)))
                .thenReturn(false);

        BDDMockito.lenient().when(mockQuestionRepository.findByTechnology(ArgumentMatchers.anyString()))
                .thenReturn(List.of(question));

        BDDMockito.lenient().when(mockCertificationRepository.saveAndFlush(ArgumentMatchers.any(Certification.class)))
                .thenReturn(certification);

    }

    @Test
    @DisplayName("execute() add new certification")
    void givenStudentCertificationAnswerRequest_whenExecute_thenReturnCertification() {
        //given
        List<StudentQuestionAnswerRequest> studentQuestionAnswers = List.of(
                StudentQuestionAnswerRequest.builder()
                        .questionId(question.getId())
                        .alternativeId(alternative.getId())
                        .isCorrect(true)
                        .build()
        );

        StudentCertificationAnswerRequest request = StudentCertificationAnswerRequest.builder()
                .email(certification.getStudent().getEmail())
                .technology(certification.getTechnology())
                .studentQuestionsAnswers(studentQuestionAnswers)
                .build();

        //when
        Certification expected = underTest.execute(request);
        //then
        BDDMockito.verify(mockStudentRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        BDDMockito.verify(mockVerifyIfHasCertificationService, Mockito.times(1))
                .execute(ArgumentMatchers.any(StudentVerifyHasCertification.class));

        BDDMockito.verify(mockQuestionRepository, Mockito.times(1))
                .findByTechnology(ArgumentMatchers.anyString());

        BDDMockito.verify(mockCertificationRepository, Mockito.times(1))
                .saveAndFlush(ArgumentMatchers.any(Certification.class));

        assertThat(expected)
                .isNotNull();
    }

    @Test
    @DisplayName("execute() throws StudentNotFoundException when student not found by id")
    void givenStudentCertificationAnswerRequest_whenExecute_thenThrowsStudentNotFoundException() {
        //config
        BDDMockito.given(mockStudentRepository.findByEmail(ArgumentMatchers.anyString()))
                .willReturn(Optional.empty());

        //given
        List<StudentQuestionAnswerRequest> studentQuestionAnswers = List.of(
                StudentQuestionAnswerRequest.builder()
                        .questionId(question.getId())
                        .alternativeId(alternative.getId())
                        .isCorrect(true)
                        .build()
        );

        StudentCertificationAnswerRequest request = StudentCertificationAnswerRequest.builder()
                .email(certification.getStudent().getEmail())
                .technology(certification.getTechnology())
                .studentQuestionsAnswers(studentQuestionAnswers)
                .build();

        //when
        assertThatThrownBy(() -> underTest.execute(request))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Estudante não existe...");

        //then
        BDDMockito.verify(mockStudentRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        BDDMockito.verify(mockVerifyIfHasCertificationService, Mockito.never())
                .execute(ArgumentMatchers.any(StudentVerifyHasCertification.class));

        BDDMockito.verify(mockQuestionRepository, Mockito.never())
                .findByTechnology(ArgumentMatchers.anyString());

        BDDMockito.verify(mockCertificationRepository, Mockito.never())
                .saveAndFlush(ArgumentMatchers.any(Certification.class));
    }

    @Test
    @DisplayName("execute() throws BusinessRulesException when Student already complete certification")
    void givenStudentCertificationAnswerRequest_whenExecute_thenStudentAlreadyCompleteCertificationThrowsBusinessRulesException() {
        //config
        BDDMockito.given(mockVerifyIfHasCertificationService
                        .execute(ArgumentMatchers.any(StudentVerifyHasCertification.class)))
                .willReturn(true);

        //given
        List<StudentQuestionAnswerRequest> studentQuestionAnswers = List.of(
                StudentQuestionAnswerRequest.builder()
                        .questionId(question.getId())
                        .alternativeId(alternative.getId())
                        .isCorrect(true)
                        .build()
        );

        StudentCertificationAnswerRequest request = StudentCertificationAnswerRequest.builder()
                .email(certification.getStudent().getEmail())
                .technology(certification.getTechnology())
                .studentQuestionsAnswers(studentQuestionAnswers)
                .build();

        //when
        assertThatThrownBy(() -> underTest.execute(request))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessage("Você já tirou sua certificação...");

        //then
        BDDMockito.verify(mockStudentRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        BDDMockito.verify(mockVerifyIfHasCertificationService, Mockito.times(1))
                .execute(ArgumentMatchers.any(StudentVerifyHasCertification.class));

        BDDMockito.verify(mockQuestionRepository, Mockito.never())
                .findByTechnology(ArgumentMatchers.anyString());

        BDDMockito.verify(mockCertificationRepository, Mockito.never())
                .saveAndFlush(ArgumentMatchers.any(Certification.class));
    }

    @Test
    @DisplayName("execute() throws BusinessRulesException when No questions were found by id")
    void givenStudentCertificationAnswerRequest_whenExecute_thenNoQuestionFoundThrowsBusinessRulesException() {
        //given
        List<StudentQuestionAnswerRequest> studentQuestionAnswers = List.of(
                StudentQuestionAnswerRequest.builder()
                        .questionId(UUID.randomUUID())
                        .alternativeId(alternative.getId())
                        .isCorrect(true)
                        .build()
        );

        StudentCertificationAnswerRequest request = StudentCertificationAnswerRequest.builder()
                .email(certification.getStudent().getEmail())
                .technology(certification.getTechnology())
                .studentQuestionsAnswers(studentQuestionAnswers)
                .build();

        //when
        assertThatThrownBy(() -> underTest.execute(request))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessage("Nenhuma pergunta foi encontrada...");

        //then
        BDDMockito.verify(mockStudentRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        BDDMockito.verify(mockVerifyIfHasCertificationService, Mockito.times(1))
                .execute(ArgumentMatchers.any(StudentVerifyHasCertification.class));

        BDDMockito.verify(mockQuestionRepository, Mockito.times(1))
                .findByTechnology(ArgumentMatchers.anyString());

        BDDMockito.verify(mockCertificationRepository, Mockito.never())
                .saveAndFlush(ArgumentMatchers.any(Certification.class));
    }

    @Test
    @DisplayName("execute() throws BusinessRulesException when No correct alternative has been found")
    void givenStudentCertificationAnswerRequest_whenExecute_thenNoCorrectAlternativeFoundThrowsBusinessRulesException() {
        //config
        alternative = AlternativeCreator.createAlternativeIncorrect(question);
        List<Alternative> alternatives = List.of(
                alternative
        );
        question.setAlternatives(alternatives);

        //given
        List<StudentQuestionAnswerRequest> studentQuestionAnswers = List.of(
                StudentQuestionAnswerRequest.builder()
                        .questionId(question.getId())
                        .alternativeId(alternative.getId())
                        .isCorrect(true)
                        .build()
        );

        StudentCertificationAnswerRequest request = StudentCertificationAnswerRequest.builder()
                .email(certification.getStudent().getEmail())
                .technology(certification.getTechnology())
                .studentQuestionsAnswers(studentQuestionAnswers)
                .build();

        //when
        assertThatThrownBy(() -> underTest.execute(request))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessage("Nenhuma alternativa correta foi encontrada...");

        //then
        BDDMockito.verify(mockStudentRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        BDDMockito.verify(mockVerifyIfHasCertificationService, Mockito.times(1))
                .execute(ArgumentMatchers.any(StudentVerifyHasCertification.class));

        BDDMockito.verify(mockQuestionRepository, Mockito.times(1))
                .findByTechnology(ArgumentMatchers.anyString());

        BDDMockito.verify(mockCertificationRepository, Mockito.never())
                .saveAndFlush(ArgumentMatchers.any(Certification.class));
    }
}