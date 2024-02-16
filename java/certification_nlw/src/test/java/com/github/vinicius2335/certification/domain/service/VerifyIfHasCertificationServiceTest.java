package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.api.representation.model.request.StudentVerifyHasCertification;
import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VerifyIfHasCertificationServiceTest {
    @InjectMocks
    private VerifyIfHasCertificationService underTest;

    @Mock
    private CertificationRepository mockCertificationRepository;

    private Certification certification;

    @BeforeEach
    void setUp() {
        Student student = StudentCreator.createStudent();
        certification = CertificationCreator.createCertification(student);

        BDDMockito.lenient().when(mockCertificationRepository.findByStudentEmailAndTechnology(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        )).thenReturn(Collections.emptyList());
    }

    @Test
    @DisplayName("execute() Return False if the student did not take the certification test for the technology")
    void givenStudentVerifyHasCertification_whenExecute_thenReturnFalse(){
        //given
        String email = certification.getStudent().getEmail();
        String technology = certification.getTechnology();
        StudentVerifyHasCertification parameters = StudentVerifyHasCertification.builder()
                .email(email)
                .technology(technology)
                .build();
        //when
        boolean expected = underTest.execute(parameters);
        //then
        BDDMockito.verify(mockCertificationRepository, Mockito.times(1))
                .findByStudentEmailAndTechnology(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        assertThat(expected)
                .isFalse();
    }

    @Test
    @DisplayName("execute() Return true if the student has already taken the certification exam for the technology provided")
    void givenStudentEmailAndTechnology_whenExecute_thenReturnTrue(){
        //config
        BDDMockito.given(mockCertificationRepository.findByStudentEmailAndTechnology(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(List.of(certification));
        //given
        String email = certification.getStudent().getEmail();
        String technology = certification.getTechnology();
        StudentVerifyHasCertification parameters = StudentVerifyHasCertification.builder()
                .email(email)
                .technology(technology)
                .build();
        //when
        boolean expected = underTest.execute(parameters);
        //then
        BDDMockito.verify(mockCertificationRepository, Mockito.times(1))
                .findByStudentEmailAndTechnology(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        assertThat(expected)
                .isTrue();
    }
}