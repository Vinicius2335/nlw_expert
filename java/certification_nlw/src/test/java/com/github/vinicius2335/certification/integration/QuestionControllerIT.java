package com.github.vinicius2335.certification.integration;

import com.github.vinicius2335.certification.utils.QuestionCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class QuestionControllerIT extends BaseIT{
    private final String basePath = "/questions";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        question = QuestionCreator.createQuestion();
    }

    @Test
    @DisplayName("findByTechnology() return status ok and return List QuestionAnswer by technology")
    void givenTechnology_whenFindByTechnology_thenReturnStatusOK(){
        initQuestionDataBase();

        RestAssured.given()
                    .pathParam("technology", "JAVA")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .get(basePath + "/technology/{technology}")
                .then()
                    .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].technology", Matchers.equalTo(question.getTechnology()))
                .log().all();
    }

    @Test
    @DisplayName("findByTechnology() return status BAD REQUEST when question not found by technology")
    void givenTechnology_whenFindByTechnology_thenReturnStatus400(){

        RestAssured.given()
                    .pathParam("technology", "JAVA")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                .when()
                    .get(basePath + "/technology/{technology}")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("title", Matchers.equalTo("No issues found related to the requested technology"))
                    .body("status", Matchers.equalTo(400))
                    .log().all();
    }

}