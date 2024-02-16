package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.domain.exception.CertificationNotFoundException;
import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DeleteCertificationServiceTest {
    @InjectMocks
    private DeleteCertificationService undertest;

    @Mock
    private CertificationRepository mockCertificationRepository;

    private Certification certification;

    @BeforeEach
    void setUp() {
        Student student = StudentCreator.createStudent();
        certification = CertificationCreator.createCertification(student);

        //execute() test config
        // lenient para não dar erro quando testar um método que não irá usar essa config
        BDDMockito.lenient().when(mockCertificationRepository.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(certification));

        BDDMockito.lenient().doNothing().when(mockCertificationRepository)
                .delete(ArgumentMatchers.any(Certification.class));
    }

    @Test
    @DisplayName("execute() remove certification from database")
    void givenID_whenExecute_thenRemoveCertification(){
        //given
        UUID id = certification.getId();
        //when
        undertest.execute(id);
        //then
        BDDMockito.verify(mockCertificationRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(Certification.class));
    }

    @Test
    @DisplayName("execute() thorws CertificationNotFoundException when certification not found by id")
    void givenId_WhenExecute_thenThrowsCertificationNotFoundException(){
        //config
        BDDMockito.given(mockCertificationRepository.findById(ArgumentMatchers.any(UUID.class)))
                .willReturn(Optional.empty());
        //given
        UUID id = certification.getId();
        //when
        Assertions.assertThatThrownBy(() -> undertest.execute(id))
                        .isInstanceOf(CertificationNotFoundException.class);
        //then
        BDDMockito.verify(mockCertificationRepository, Mockito.never())
                .delete(ArgumentMatchers.any(Certification.class));
    }
}