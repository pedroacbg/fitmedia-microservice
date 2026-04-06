package com.fitmedia.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmedia.aiservice.model.Activity;
import com.fitmedia.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI GENERATED");
        return processAIResponse(activity, aiResponse);
    }

    private Recommendation processAIResponse(Activity activity, String aiResponse){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText().replaceAll("```json\\n", "")
                    .replace("\\n```", "").replaceAll("```", "").trim();

            log.info("PARSED RESPONSE FROM AI");
            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            Recommendation recommendationObj = new Recommendation();
            recommendationObj.setActivityId(activity.getId());
            recommendationObj.setUserId(activity.getUserId());
            recommendationObj.setActivityType(activity.getType());
            recommendationObj.setRecommendation(fullAnalysis.toString().trim());
            recommendationObj.setImprovements(improvements);
            recommendationObj.setSuggestions(suggestions);
            recommendationObj.setSafety(safety);
            recommendationObj.setCreatedAt(LocalDateTime.now());
            return recommendationObj;
        }catch(Exception e){
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        Recommendation recommendationObj = new Recommendation();
        recommendationObj.setActivityId(activity.getId());
        recommendationObj.setUserId(activity.getUserId());
        recommendationObj.setActivityType(activity.getType());
        recommendationObj.setRecommendation("Incapaz de gerar uma análise detalhada");
        recommendationObj.setImprovements(Collections.singletonList("Continue com a sua rotine atual"));
        recommendationObj.setSuggestions(Collections.singletonList("Considere consultar um profissional"));
        recommendationObj.setSafety(Arrays.asList("Sempre aqueça antes dos exercícios", "Se mantenha hidratado", "Preste atenção em seu corpo"));
        recommendationObj.setCreatedAt(LocalDateTime.now());
        return recommendationObj;
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safeties = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(safety -> safeties.add(safety.asText()));
        }
        return safeties.isEmpty() ? Collections.singletonList("Follow standard safety guidelines") : safeties;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestions.isEmpty() ? Collections.singletonList("No specific suggestions provided") : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            });
        }
        return improvements.isEmpty() ? Collections.singletonList("No specific improvements provided") : improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix).append(analysisNode.path(key).asText()).append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                Analise essa atividade fitness e gere de forma detalhada recomendações seguindo exatamente esse formato de JSON em português do Brasil:
                {
                    "analysis": {
                        "overall": "Análise overall aqui",
                        "pace": "Análise de pace aqui",
                        "heartRate": "Análise de frequência cardíaca aqui",
                        "caloriesBurned": "Análise de calorias queimadas aqui",
                    },
                    "improvements": [
                        {
                            "area": "Nome da área",
                            "recommendation": "Recomendação detalhada"
                        }
                    ],
                    "suggestions": [
                        {
                            "workout": "Nome do exercício",
                            "description": "Descrição detalhada do exercício"
                        }
                    ],
                    "safety": [
                        "Ponto de segurança 1",
                        "Ponto de segurança 2"
                    ]
                }
                
                Analise essa atividade:
                Activity Type: %s
                Duration: %d minutos
                Calories Burned: %d
                Additional Metrics: %s
                
                Gere uma análise detalhada focando na performance, melhorias, sugestões de próximos exercícios e guias de segurança.
                Tenha certeza que a resposta siga o formato EXATO do JSON mostrado acima.
                """,
                    activity.getType(), activity.getDuration(), activity.getCaloriesBurned(), activity.getAdditionalMetrics());
    }

}
