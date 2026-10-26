package com.enterprise.docintel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmbeddingService {

    @Value("${google.ai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateEmbedding(String text) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key=" + apiKey;

        try {
            // Construct Request Body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(Collections.singletonMap("text", text)));
            requestBody.put("model", "models/gemini-embedding-001");
            requestBody.put("content", content);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // Parse Response
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode values = root.path("embedding").path("values");

            if (values.isArray()) {
                List<Double> vector = new ArrayList<>();
                for (JsonNode node : values) {
                    vector.add(node.asDouble());
                }
                return "[" + vector.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")) + "]";
            }
        } catch (Exception e) {
            System.err.println("Error generating embedding: " + e.getMessage());
        }

        // Fallback to a zero-vector if API fails to prevent system crash
        return "[" + String.join(",", Collections.nCopies(768, "0")) + "]";
    }
}
