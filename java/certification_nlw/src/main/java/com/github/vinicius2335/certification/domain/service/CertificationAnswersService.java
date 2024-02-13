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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationAnswersService {
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final CertificationRepository certificationRepository;
    private final VerifyIfHasCertificationService verifyIfHasCertificationService;

    @Transactional
    public Certification execute(StudentCertificationAnswerRequest request) {
        // Verificar se o estudante existe
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new StudentNotFoundException("Estudante não existe..."));

        // Verifica se o estudante já realizou a prova de certificação
        StudentVerifyHasCertification verifyStudent = StudentVerifyHasCertification.builder()
                .email(request.getEmail())
                .technology(request.getTechnology())
                .build();

        boolean hasCertification = verifyIfHasCertificationService.execute(verifyStudent);

        if(hasCertification){
            throw new BusinessRulesException("Você já tirou sua certificação...");
        }

        // Buscar as alternativas das perguntas para verificar se está correta ou não
        List<Question> questionsByTechnology = questionRepository.findByTechnology(request.getTechnology());

        // Cria uma certificação
        Certification certification = Certification.builder()
                .student(student)
                .technology(request.getTechnology())
                .grate(0)
                .answersCertificationList(new ArrayList<>())
                .build();

        // Para cada pergunta que o estudante respondeu
        request.getStudentQuestionsAnswers()
                .forEach(studentQuestionAnswer -> {
                    // Procura a pergunta
                    Question questionAnswer = questionsByTechnology.stream()
                            .filter(question -> question.getId().equals(studentQuestionAnswer.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() -> new BusinessRulesException("Nenhuma pergunta foi encontrada..."));

                    // Procura a alternativa correta
                    Alternative correctAlternative = questionAnswer.getAlternatives().stream()
                            .filter(Alternative::isCorrect)
                            .findFirst()
                            .orElseThrow(() -> new BusinessRulesException("Nenhuma alternativa correta foi encontrada..."));

                    // Verifica se o estudante acertou a resposta
                    studentQuestionAnswer.setCorrect(
                            studentQuestionAnswer.getAlternativeId().equals(correctAlternative.getId())
                    );

                    // Adiciona a pergunta, resposta e o resultado na certificação
                    certification.addAnswersCertification(
                            studentQuestionAnswer.getQuestionId(),
                            studentQuestionAnswer.getAlternativeId(),
                            studentQuestionAnswer.isCorrect()
                    );
                });

        // Calcula e adiciona a nota na grade
        int grade = calculateGrade(request);
        certification.setGrate(grade);

        // Salvar as informações da certificação
        certificationRepository.saveAndFlush(certification);

        return certification;
    }

    private int calculateGrade(StudentCertificationAnswerRequest request){
        long totalQuestions = request.getStudentQuestionsAnswers().size();

        long totalCorrectQuestions = request.getStudentQuestionsAnswers().stream()
                .filter(StudentQuestionAnswerRequest::isCorrect)
                .count();

        return Math.round(((float) totalCorrectQuestions / totalQuestions) * 10);
    }
}
