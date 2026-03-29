package org.example.ceylontraditionalmedicinecenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ceylontraditionalmedicinecenter.dto.AiDiagnosisDTO;
import org.example.ceylontraditionalmedicinecenter.entity.AiDiagnosis;
import org.example.ceylontraditionalmedicinecenter.repository.AiDiagnosisRepository;
import org.example.ceylontraditionalmedicinecenter.service.AiDiagnosisService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiDiagnosisServiceImpl implements AiDiagnosisService {

    @Autowired
    private AiDiagnosisRepository aiDiagnosisRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.api.key:YOUR_OPENAI_API_KEY}")
    private String openaiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public AiDiagnosisDTO diagnoseSymptoms(AiDiagnosisDTO diagnosisDTO) {
        try {
            // Create the AI prompt
            String prompt = createAyurvedicPrompt(diagnosisDTO.getSymptoms());

            // Call OpenAI API
            String aiResponse = callOpenAI(prompt);

            // Save to database
            AiDiagnosis aiDiagnosis = new AiDiagnosis();
            aiDiagnosis.setSymptoms(diagnosisDTO.getSymptoms());
            aiDiagnosis.setDiagnosis(aiResponse);
            aiDiagnosis.setUserEmail(diagnosisDTO.getUserEmail());
            aiDiagnosis.setCreatedAt(LocalDateTime.now());
            aiDiagnosis.setLanguage(diagnosisDTO.getLanguage());

            aiDiagnosisRepository.save(aiDiagnosis);

            // Return response
            diagnosisDTO.setDiagnosis(aiResponse);
            diagnosisDTO.setId(aiDiagnosis.getId());
            diagnosisDTO.setCreatedAt(aiDiagnosis.getCreatedAt());

            return diagnosisDTO;

        } catch (Exception e) {
            throw new RuntimeException("AI diagnosis failed: " + e.getMessage());
        }
    }

    private String createAyurvedicPrompt(String symptoms) {
        return "ඔබ ආයුර්වේද සහ හෙළ වෙදකමේ විශේෂඥයෙක් වන අතර සිංහල භාෂාවෙන් පිළිතුරු සපයන්න.\n\n" +
               "පහත දක්වා ඇති රෝග ලක්ෂණ මත පදනම්ව සුදුසු සහ සුරක්ෂිත සිංහල ඖෂධ සහ ප්‍රාකෘතික ප්‍රතිකාර යෝජනා කරන්න:\n\n" +
               "රෝග ලක්ෂණ: " + symptoms + "\n\n" +
               "කරුණාකර පිළිතුරේ සහඳන්න:\n" +
               "1. හැකි රෝගයේ සරල විස්තරයක්\n" +
               "2. යෝජනා කරන සිංහල ඖෂධ සහ වෛද්‍ය ප්‍රතිකාර\n" +
               "3. ප්‍රාකෘතික යෝජනා (ආහාර, ගෝල්ඩන්, ජීවන රටාව)\n\n" +
               "වැදගත්: මෙය වෛද්‍ය උපදෙස් නොවන බව සහ සුදුසු වෛද්‍යවරයෙකුගෙන් උපදෙස් ලබා ගැනීම අවශ්‍ය බව මතක් කරන්න.";
    }

    private String callOpenAI(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", new Object[]{
                Map.of("role", "system", "content", "ඔබ ආයුර්වේද සහ හෙළ වෙදකමේ විශේෂඥයෙක් වන අතර සිංහල භාෂාවෙන් පිළිතුරු සපයන්න."),
                Map.of("role", "user", "content", prompt)
            });
            requestBody.put("max_tokens", 1000);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                return root.path("choices").get(0).path("message").path("content").asText();
            } else {
                throw new RuntimeException("OpenAI API call failed with status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            // Fallback response in case of API failure
            return "සමාවන්න, මේ මොහොතේ AI සේවාවට ප්‍රවේශ විය නොහැක. කරුණාකර පසුව උත්සාහ කරන්න හෝ සුදුසු වෛද්‍යවරයෙකුගෙන් උපදෙස් ලබා ගන්න.\n\n" +
                   "වැදගත්: මෙය වෛද්‍ය උපදෙස් නොවන බව සහ සුදුසු වෛද්‍යවරයෙකුගෙන් උපදෙස් ලබා ගැනීම අවශ්‍ය බව මතක් කරන්න.";
        }
    }
}