package com.enterprise.docintel.service;

import com.enterprise.docintel.model.DocumentChunk;
import com.enterprise.docintel.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class RagService {

    private final DocumentChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;

    @Value("${google.ai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askQuestion(String question) {
        // 1. Generate embedding for the question
        String queryVector = embeddingService.generateEmbedding(question);

        // 2. Retrieve top K relevant chunks from pgvector
        List<DocumentChunk> relevantChunks = chunkRepository.findRelevantChunks(queryVector, 3);

        String context = relevantChunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n\n"));

        // 3. Build prompt for LLM
        String prompt = "You are an enterprise assistant. Use the following context to answer the question. " +
                        "If the answer is not in the context, say that you don't know.\n\n" +
                        "Context:\n" + context + "\n\n" +
                        "Question: " + question + "\n\n" +
                        "Answer:";

        // 4. Call Gemini API for real response
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + apiKey;

            // Construct Request Body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);
            content.put("parts", Collections.singletonList(part));
            requestBody.put("contents", Collections.singletonList(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // Parse Response
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates")
                       .get(0)
                       .path("content")
                       .path("parts")
                       .get(0)
                       .path("text")
                       .asText();

        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            return "I'm sorry, I encountered an error while generating the response. Please try again.";
        }
    }
}
