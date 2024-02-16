package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Student;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import com.github.vinicius2335.certification.utils.CertificationCreator;
import com.github.vinicius2335.certification.utils.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TopRankingServiceTest {
    @InjectMocks
    private TopRankingService underTest;

    @Mock
    private CertificationRepository mockCertificationRespository;

    private Certification certification;

    @BeforeEach
    void setUp() {
        Student student = StudentCreator.createStudent();
        certification = CertificationCreator.createCertification(student);
        BDDMockito.given(mockCertificationRespository.findTop10ByOrderByGrateDesc()).willReturn(List.of(certification));
    }

    @Test
    @DisplayName("execute() return list of top 10 certification order by grate")
    void whenExecute_thenReturnListCertification(){
        //when
        List<Certification> expected = underTest.execute();
        //then
        BDDMockito.verify(mockCertificationRespository, Mockito.times(1))
                .findTop10ByOrderByGrateDesc();

        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(certification);
    }
}