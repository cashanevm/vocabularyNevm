package com.example.vocabularynevm.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.example.vocabularynevm.Model.WordDefinition;
import com.example.vocabularynevm.Model.WordDescription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordApi {

    private final ObjectMapper mapper;

    public WordDescription getDescription(String word) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://wordsapiv1.p.rapidapi.com/words/" + word))
                    .header("X-RapidAPI-Key", "63d7cd07a8msh4205b46472d440bp1e0e63jsn6c5d5cbac17d")
                    .header("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

            return WordDescription.builder()
                    .all(response)
                    .pronunciation(getPronunciation(response))
                    .definitions(
                            getDefinitions(response)
                    )
                    .examples(getExamples(word))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getExamples(String word) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://wordsapiv1.p.rapidapi.com/words/"+word+"/examples"))
                .header("X-RapidAPI-Key", "63d7cd07a8msh4205b46472d440bp1e0e63jsn6c5d5cbac17d")
                .header("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode jsonNode = mapper.readTree(response.body());

        jsonNode = jsonNode.findValue("examples");

        ArrayList<String> strings = new ArrayList<>();


        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode node:((ArrayNode) jsonNode)) {
                strings.add(node.asText());
            }
        } else {
            throw new RuntimeException();
        }

        return strings;
    }

    private List<WordDefinition> getDefinitions(String response) {
        try {
            JsonNode jsonNode = mapper.readTree(response);

            JsonNode definitions = jsonNode.findValue("results");

            if (definitions == null) {
                definitions = jsonNode.findValue("definitions");
            }

            if (definitions != null && definitions.isArray()) {
                ArrayNode arrayNode = (ArrayNode) definitions;

                ArrayList<WordDefinition> result = new ArrayList<>();

                for (JsonNode node : arrayNode) {
                    result.add(mapper.readValue(mapper.writeValueAsString(node), WordDefinition.class));
                }

                return result;
            } else {
                log.error("definitions " + definitions);

                return null;
            }

        } catch (JsonProcessingException e) {
            log.error("Definitions", e);

            return null;
        }
    }

    private String getPronunciation(String all) {
        try {
            JsonNode jsonNode = mapper.readTree(all);

            JsonNode pronunciation = jsonNode.findValue("pronunciation");

            if (pronunciation.isTextual()) {
                return pronunciation.asText();
            } else if (pronunciation.isObject()) {
                return pronunciation.findValue("all").asText();
            } else {
                log.error(pronunciation.asText());

                return null;
            }

        } catch (JsonProcessingException e) {
            log.error("pronunciation", e);

            return null;
        }
    }
}
