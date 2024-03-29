package com.nevm.vocabulary.service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDefinition {
    private String definition;

    private String partOfSpeech;

    private List<String> synonyms;
}
