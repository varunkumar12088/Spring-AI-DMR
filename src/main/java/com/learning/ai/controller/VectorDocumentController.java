package com.learning.ai.controller;

import com.learning.ai.helper.Helper;
import com.learning.ai.service.VectorDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vector")
public class VectorDocumentController {

    @Autowired
    private VectorDocumentService vectorDocumentService;

    @GetMapping("/java")
    public String storeJavaConcept(){
        vectorDocumentService.addDocuments(Helper.getJavaConcepts());
        return "Done";
    }

}
