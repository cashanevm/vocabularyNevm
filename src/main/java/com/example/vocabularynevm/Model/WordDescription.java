package com.example.vocabularynevm.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordDescription {
    private String pronunciation;

    private List<WordDefinition> definitions;

    private String all;
}
