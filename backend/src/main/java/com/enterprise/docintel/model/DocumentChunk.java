package com.enterprise.docintel.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "document_chunks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int chunkIndex;

    // We store embeddings as a double array.
    // In a real production app with pgvector, we'd use a custom type or a string representation
    // that pgvector can cast to 'vector'.
    @Column(columnDefinition = "vector(3072)")
    private String embedding;
}
