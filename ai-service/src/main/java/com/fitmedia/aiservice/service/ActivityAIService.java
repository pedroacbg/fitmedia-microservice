package com.fitmedia.aiservice.service;

import com.fitmedia.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public String generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {}", aiResponse);
        return aiResponse;
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
