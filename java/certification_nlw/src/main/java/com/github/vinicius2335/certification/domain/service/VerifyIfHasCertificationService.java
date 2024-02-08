package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.api.representation.model.request.StudentVerifyHasCertification;
import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerifyIfHasCertificationService {

    private final CertificationRepository certificationRepository;

    public boolean execute(StudentVerifyHasCertification request){
        List<Certification> result = certificationRepository.findByStudentEmailAndTechnology(
                request.getEmail(),
                request.getTechnology()
        );

        // Se encontrar  alguma certification retorna true, se não encontrar false
        return !result.isEmpty();
    }
}
