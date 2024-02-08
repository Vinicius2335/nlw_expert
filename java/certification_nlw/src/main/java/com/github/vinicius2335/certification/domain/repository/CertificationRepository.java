package com.github.vinicius2335.certification.domain.repository;

import com.github.vinicius2335.certification.domain.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CertificationRepository extends JpaRepository<Certification, UUID> {

    @Query("SELECT c FROM Certification c INNER JOIN c.student std WHERE std.email = :email AND c.technology = :technology")
    List<Certification> findByStudentEmailAndTechnology(String email, String technology);

    List<Certification> findTop10ByOrderByGrateDesc();
}
