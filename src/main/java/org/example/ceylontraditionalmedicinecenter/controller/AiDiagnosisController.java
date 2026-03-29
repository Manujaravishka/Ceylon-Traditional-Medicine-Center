package org.example.ceylontraditionalmedicinecenter.controller;

import lombok.RequiredArgsConstructor;
import org.example.ceylontraditionalmedicinecenter.dto.AiDiagnosisDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.AiDiagnosisService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AiDiagnosisController {

    private final AiDiagnosisService aiDiagnosisService;

    @PostMapping("/ai-diagnose")
    public ResponseEntity<ResponseDTO> diagnoseSymptoms(@RequestBody AiDiagnosisDTO diagnosisDTO) {
        try {
            // Set default language if not provided
            if (diagnosisDTO.getLanguage() == null || diagnosisDTO.getLanguage().isEmpty()) {
                diagnosisDTO.setLanguage("sinhala");
            }

            AiDiagnosisDTO result = aiDiagnosisService.diagnoseSymptoms(diagnosisDTO);

            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "AI diagnosis completed successfully", result));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,
                        "AI diagnosis failed: " + e.getMessage(),
                        "සමාවන්න, මේ මොහොතේ AI සේවාවට ප්‍රවේශ විය නොහැක. කරුණාකර පසුව උත්සාහ කරන්න."));
        }
    }
}