package com.learning.ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DocumentRequest {

    private List<String> documents;
}
