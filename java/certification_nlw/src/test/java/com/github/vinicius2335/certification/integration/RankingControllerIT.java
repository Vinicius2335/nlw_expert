package com.github.vinicius2335.certification.integration;

import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;


class RankingControllerIT extends BaseIT{
    private final String basePath = "/ranking";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        enableLoggingOfRequestAndResponseIfValidationFails();

        student = StudentCreator.createStudent();
        certification = CertificationCreator.createCertification(student);
    }

    @Test
    @DisplayName("topRanking() return status OK and List Certification")
    void givenURL_whenTopRanking_thenStatus200(){
        initCertificationDataBase();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .get(basePath + "/top10")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].student.id", Matchers.equalTo(student.getId().toString()))
                .log().all();
    }


}