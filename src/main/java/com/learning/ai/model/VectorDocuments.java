package com.learning.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collation = "vector_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VectorDocuments {

    @Id
    private String id;
    private String content;
    private List<Double> embeddings;
    private Map<String, Object> metadata;

}
