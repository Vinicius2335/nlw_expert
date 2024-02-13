package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.domain.exception.CertificationNotFoundException;
import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCertificationService {
    private final CertificationRepository certificationRepository;

    @Transactional
    public void execute(UUID certificationId){
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new CertificationNotFoundException("Certificado não encontrado..."));

        certificationRepository.delete(certification);
    }
}
