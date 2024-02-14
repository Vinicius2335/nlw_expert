package com.github.vinicius2335.certification.utils;

import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public abstract class CertificationCreator {

    public static Certification createCertification(Student student){
        return Certification.builder()
                .id(UUID.randomUUID())
                .student(student)
                .grate(7)
                .technology("JAVA")
                .createdAt(LocalDateTime.now())
                .answersCertificationList(new ArrayList<>())
                .build();
    }
}
