package com.github.vinicius2335.certification.integration;

import com.github.vinicius2335.certification.api.representation.model.request.StudentCertificationAnswerRequest;
import com.github.vinicius2335.certification.api.representation.model.request.StudentQuestionAnswerRequest;
import com.github.vinicius2335.certification.api.representation.model.request.StudentVerifyHasCertification;
import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.QuestionCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

class StudentControllerIT extends BaseIT{
    private final String basePath = "/students";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        student = StudentCreator.createStudent();
        question = QuestionCreator.createQuestion();
        certification = CertificationCreator.createCertification(student);
    }

    @Test
    @DisplayName("verifyHasCertification() return message when student can take the certification test")
    void givenStudentEmailAndTechnology_whenVerifyHasCertification_thenStatus200(){
        initQuestionDataBase();

        StudentVerifyHasCertification request = StudentVerifyHasCertification.builder()
                .email(student.getEmail())
                .technology(question.getTechnology())
                .build();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/verifyHasCertification")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", Matchers.equalTo("Usuário pode fazer a prova"))
                .log().all();
    }

    @Test
    @DisplayName("verifyHasCertification() return message when student has already taken the certification test")
    void givenStudentEmailAndTechnology_whenVerifyHasCertification_thenStatus400(){
        initQuestionDataBase();
        initCertificationDataBase();

        StudentVerifyHasCertification request = StudentVerifyHasCertification.builder()
                .email(student.getEmail())
                .technology(question.getTechnology())
                .build();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/verifyHasCertification")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Usuário já realizou a prova"))
                .log().all();
    }

    @Test
    @DisplayName("certificationAnswer() return certification when student submitted responses related a certification")
    void givenRequest_whenCertificationAnswer_thenStatusOK(){
        initStudentDataBase();
        initAlternativeDataBase();
        initQuestionDataBase();

        StudentCertificationAnswerRequest request = getStudentCertificationAnswerRequest();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/certification/answer")
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    private StudentCertificationAnswerRequest getStudentCertificationAnswerRequest() {
        StudentQuestionAnswerRequest studentQuestionAnswerRequest = StudentQuestionAnswerRequest.builder()
                .questionId(question.getId())
                .alternativeId(alternative.getId())
                .isCorrect(true)
                .build();

        return StudentCertificationAnswerRequest.builder()
                .email(student.getEmail())
                .technology(question.getTechnology())
                .studentQuestionsAnswers(new ArrayList<>(List.of(studentQuestionAnswerRequest)))
                .build();
    }

    @Test
    @DisplayName("certificationAnswer() throws exception when student not found")
    void givenRequest_whenCertificationAnswer_thenStudentNotFoundStatus404(){
        initAlternativeDataBase();
        initQuestionDataBase();

        StudentCertificationAnswerRequest request = getStudentCertificationAnswerRequest();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/certification/answer")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Estudante não existe..."))
                .log().all();
    }

    @Test
    @DisplayName("certificationAnswer() throws exception when certification already completed")
    void givenRequest_whenCertificationAnswer_thenCertificationAlreadyCompletedStatus400(){
        initAlternativeDataBase();
        initQuestionDataBase();
        initCertificationDataBase();

        StudentCertificationAnswerRequest request = getStudentCertificationAnswerRequest();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/certification/answer")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Você já tirou sua certificação..."))
                .log().all();
    }

    @Test
    @DisplayName("certificationAnswer() throws exception when question not found")
    void givenRequest_whenCertificationAnswer_thenQuestionNotFoundAndStatus400(){
        initStudentDataBase();
        initAlternativeDataBase();

        StudentCertificationAnswerRequest request = getStudentCertificationAnswerRequest();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/certification/answer")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Nenhuma pergunta foi encontrada..."))
                .log().all();
    }

    @Test
    @DisplayName("certificationAnswer() throws exception when no correct alternative has been found...")
    void givenRequest_whenCertificationAnswer_thenNoCorrectAlternativeFoundAndStatus400(){
        initStudentDataBase();
        initQuestionDataBase();
        initAlternativeDataBase();

        StudentCertificationAnswerRequest request = getStudentCertificationAnswerRequest();

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/certification/answer")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Nenhuma alternativa correta foi encontrada..."))
                .log().all();
    }

    @Test
    @DisplayName("certificationAnswer() throws exception when request have invalid fields")
    void givenRequest_whenCertificationAnswer_thenRequestHaveInvalidFieldAndStatus400(){
        initStudentDataBase();
        initQuestionDataBase();
        initAlternativeDataBase();

        StudentCertificationAnswerRequest request = getStudentCertificationAnswerRequest();
        request.setTechnology("");

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post(basePath + "/certification/answer")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Um ou mais campos estão inválido. Faça o preenchimento correto e tente novamente."))
                .log().all();
    }

    @Test
    @DisplayName("deleteCertificationService() remove certification")
    void givenCertificationID_whenDeleteCertificationService_thenStatusOK(){
        initCertificationDataBase();

        RestAssured.given()
                .pathParam("certificationId", certification.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .delete(basePath + "/certification/{certificationId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }
}