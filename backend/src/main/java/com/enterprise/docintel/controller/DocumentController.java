package com.enterprise.docintel.controller;

import com.enterprise.docintel.model.DocumentChunk;
import com.enterprise.docintel.repository.DocumentChunkRepository;
import com.enterprise.docintel.service.DocumentService;
import com.enterprise.docintel.service.EmbeddingService;
import com.enterprise.docintel.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/docs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class DocumentController {

    private final DocumentService documentService;
    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository chunkRepository;
    private final RagService ragService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            List<String> chunks = documentService.processPdf(file);
            for (int i = 0; i < chunks.size(); i++) {
                String content = chunks.get(i);
                String embedding = embeddingService.generateEmbedding(content);

                chunkRepository.insertChunk(i, content, embedding, file.getOriginalFilename());
            }
            return ResponseEntity.ok("Document uploaded and indexed successfully!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing PDF: " + e.getMessage());
        }
    }

    @GetMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestParam("q") String question) {
        String answer = ragService.askQuestion(question);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/debug/models")
    public ResponseEntity<String> listModels() {
        try {
            RestTemplate rt = new RestTemplate();
            String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" +
                         documentService.getApiKey(); // I'll need to make sure we can get the key
            return ResponseEntity.ok(rt.getForObject(url, String.class));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
