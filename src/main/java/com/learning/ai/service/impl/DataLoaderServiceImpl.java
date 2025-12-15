package com.learning.ai.service.impl;

import com.learning.ai.service.DataLoaderService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderServiceImpl implements DataLoaderService {

    @Value("classpath:sample_data.json")
    private Resource jsonResource;

    @Value("classpath:cricket_rules.pdf")
    private Resource pdfResource;

    @Override
    public List<Document> loadDocumentsFromJson() {
        var jsonReader = new JsonReader(jsonResource);
        return jsonReader.read();
    }

    @Override
    public List<Document> loadDocumentsFromPdf() {
        var pdfReader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build()
                );
        return pdfReader.read();
    }
}
