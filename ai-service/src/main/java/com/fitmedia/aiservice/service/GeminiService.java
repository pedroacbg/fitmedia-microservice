package com.fitmedia.aiservice.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question){
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                }
        );
        String response = webClient.post().uri(geminiUrl).headers(headers -> {
            headers.put("Content-Type", Collections.singletonList("application/json"));
            headers.put("X-goog-api-key", Collections.singletonList(geminiApiKey));
        }).bodyValue(requestBody)
                .retrieve().bodyToMono(String.class).block();
        return response;
    }

}
