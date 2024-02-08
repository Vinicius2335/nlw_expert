package com.github.vinicius2335.certification.domain.service;

import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopRankingService {
    private final CertificationRepository certificationRepository;

    public List<Certification> execute(){
        return certificationRepository.findTop10ByOrderByGrateDesc();
    }

}
