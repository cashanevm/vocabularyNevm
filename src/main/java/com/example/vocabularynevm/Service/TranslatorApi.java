package com.example.vocabularynevm.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslatorApi {

    private final ObjectMapper mapper;

    public String translate(String original) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://text-translator2.p.rapidapi.com/translate"))
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("X-RapidAPI-Key", "63d7cd07a8msh4205b46472d440bp1e0e63jsn6c5d5cbac17d")
                    .header("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                    .method("POST", HttpRequest.BodyPublishers.ofString("source_language=en&target_language=uk&text=" + original))
                    .build();
            String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

            JsonNode jsonNode = mapper.readTree(response);

            return jsonNode.findValue("data").findValue("translatedText").asText();

        } catch (Exception e) {
            log.error("TranslatorApi", e);

            throw new RuntimeException(e);
        }
    }
}
