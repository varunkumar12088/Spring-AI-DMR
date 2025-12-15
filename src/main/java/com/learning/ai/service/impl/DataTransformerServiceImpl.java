package com.learning.ai.service.impl;

import com.learning.ai.service.DataTransformerService;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTransformerServiceImpl implements DataTransformerService {

    @Override
    public List<Document> transform(List<Document> documents) {
        var splitter = new TokenTextSplitter(500, 400, 10, 5000, true);
        return splitter.transform(documents);
    }
}
