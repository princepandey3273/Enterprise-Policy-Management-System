package com.enterprise.docintel.repository;

import com.enterprise.docintel.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO document_chunks (chunk_index, content, embedding, file_name) VALUES (:chunkIndex, :content, cast(:embedding as vector), :fileName)", nativeQuery = true)
    void insertChunk(@Param("chunkIndex") int chunkIndex, @Param("content") String content, @Param("embedding") String embedding, @Param("fileName") String fileName);

    // Custom query for pgvector similarity search.
    // The <=> operator in pgvector calculates cosine distance.
    @Query(value = "SELECT * FROM document_chunks ORDER BY embedding <=> cast(:embedding as vector) LIMIT :limit", nativeQuery = true)
    List<DocumentChunk> findRelevantChunks(@Param("embedding") String embedding, @Param("limit") int limit);
}
