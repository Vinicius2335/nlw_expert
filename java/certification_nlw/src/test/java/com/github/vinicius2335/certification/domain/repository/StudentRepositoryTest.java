package com.github.vinicius2335.certification.domain.repository;

import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.utils.StudentCreator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
class StudentRepositoryTest {
    @Autowired
    private StudentRepository underTest;

    @Autowired
    private EntityManager entityManager;

    private Student student;

    @BeforeEach
    void setUp() {
        student = StudentCreator.createStudent();
    }

    @Test
    @DisplayName("saveAndFlush() save new student")
    void givenStudent_whenSaveAndFlush_thenInsertAndReturnNewStudent(){
        //given
        Student studentToSave = student;
        //when
        Student expected = underTest.saveAndFlush(student);
        //then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(studentToSave);
    }

    private Student saveAndReturnStudent(){
         return underTest.saveAndFlush(student);
    }

    @Test
    @DisplayName("saveAndFlush() throws error when student alread exist by email")
    void givenStudent_whenSaveAndFlush_thenThrowsException(){
        //given
        Student studentSaved = saveAndReturnStudent();
        Student studentToSave = Student.builder()
                .id(UUID.randomUUID())
                .email(studentSaved.getEmail())
                .createdAt(LocalDateTime.now())
                .certifications(new ArrayList<>())
                .build();
        //then
        assertThatThrownBy(() -> underTest.saveAndFlush(studentToSave))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("findByEmail() return optional student")
    void givenEmail_whenFindByEmail_thenReturnOptionalStudent(){
        //given
        Student studentSaved = saveAndReturnStudent();
        String email = studentSaved.getEmail();
        //when
        Optional<Student> expected = underTest.findByEmail(email);
        //then
        assertThat(expected)
                .isPresent()
                .hasValue(studentSaved);
    }

    @Test
    @DisplayName("findByEmail() return empty optional when student not found by email")
    void givenEmail_whenFindByName_thenReturnEmptyOptionalStudent(){
        //given
        String email = student.getEmail();
        //when
        Optional<Student> expected = underTest.findByEmail(email);
        //then
        assertThat(expected)
                .isEmpty();
    }

}