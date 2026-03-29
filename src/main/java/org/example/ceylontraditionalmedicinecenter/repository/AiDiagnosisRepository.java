package org.example.ceylontraditionalmedicinecenter.repository;

import org.example.ceylontraditionalmedicinecenter.entity.AiDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiDiagnosisRepository extends JpaRepository<AiDiagnosis, Long> {
    List<AiDiagnosis> findByUserEmail(String userEmail);
    List<AiDiagnosis> findTop10ByOrderByCreatedAtDesc();
}