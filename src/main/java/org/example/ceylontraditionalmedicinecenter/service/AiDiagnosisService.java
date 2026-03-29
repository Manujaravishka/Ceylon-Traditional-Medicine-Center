package org.example.ceylontraditionalmedicinecenter.service;

import org.example.ceylontraditionalmedicinecenter.dto.AiDiagnosisDTO;

public interface AiDiagnosisService {
    AiDiagnosisDTO diagnoseSymptoms(AiDiagnosisDTO diagnosisDTO);
}