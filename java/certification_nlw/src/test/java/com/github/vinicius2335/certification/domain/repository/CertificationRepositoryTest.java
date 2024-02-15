package com.github.vinicius2335.certification.domain.repository;

import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CertificationRepositoryTest {

    @Autowired
    private CertificationRepository underTest;
    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        student = studentRepository.saveAndFlush(StudentCreator.createStudent());
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("saveAndFlush() save certification")
    void givenCertification_whenSaveAndFlush_thenCertificationShouldBeInserted () {
        // given
        Certification certificationToSave = CertificationCreator.createCertification(student);
        // when
        Certification expected = underTest.saveAndFlush(certificationToSave);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(certificationToSave);
    }

    private Certification saveAndReturnNewCertification(){
        Certification certification = CertificationCreator.createCertification(student);
        return underTest.saveAndFlush(certification);
    }

    @Test
    @DisplayName("findByStudentEmailAndTechnology() return list of certification")
    void givenEmailAndTechnology_whenFindByStudentEmailAndTechnology_thenReturnListCertifications(){
        //given
        Certification certification = saveAndReturnNewCertification();
        String email = student.getEmail();
        String technology = "JAVA";
        //when
        List<Certification> expected = underTest.findByStudentEmailAndTechnology(email, technology);
        //then
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(certification);
    }

    @Test
    @DisplayName("findTop10ByOrderByGrateDesc() return list top 10 certification by grate")
    void whenFindTop10ByOrderByGrateDesc_thenReturnListTop10CertificationByGrate(){
        //given
        Certification certification = saveAndReturnNewCertification();
        //when
        List<Certification> expected = underTest.findTop10ByOrderByGrateDesc();
        //then
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(certification);
    }

    @Test
    @DisplayName("findById() return optional of certification")
    void givenUUID_whenFindById_thenReturnOptionalCertification(){
        //given
        Certification certification = saveAndReturnNewCertification();
        //when
        Optional<Certification> exception = underTest.findById(certification.getId());
        //then
        assertThat(exception)
                .isNotEmpty()
                .contains(certification);
    }

    @Test
    @DisplayName("findById() return empty option of certification")
    void givenUUID_whenFindById_thenReturnEmptyOptionalCertification(){
        //given
        saveAndReturnNewCertification();
        UUID id = UUID.randomUUID();
        //when
        Optional<Certification> expected = underTest.findById(id);
        //then
        assertThat(expected)
                .isEmpty();
    }

    // delete
    @Test
    @DisplayName("delete() remove certification")
    void givenCertification_whenDelete_thenRemoveCertificationFromDatabase(){
        //given
        Certification certification = saveAndReturnNewCertification();
        //when
        underTest.delete(certification);
        //then
        List<Certification> expected = underTest.findAll();
        assertThat(expected)
                .isEmpty();
    }
}