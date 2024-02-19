package com.github.vinicius2335.certification.integration;

import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Question;
import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import com.github.vinicius2335.certification.domain.repository.QuestionRepository;
import com.github.vinicius2335.certification.domain.repository.StudentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {
    @LocalServerPort
    protected int port;

    protected Student student;
    protected Certification certification;
    protected Question question;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CertificationRepository certificationRepository;
    @Autowired
    private QuestionRepository questionRepository;

    protected void initCertificationDataBase(){
        studentRepository.saveAndFlush(student);
        certificationRepository.saveAndFlush(certification);
    }

    protected void initQuestionDataBase(){
        questionRepository.saveAndFlush(question);
    }
}
