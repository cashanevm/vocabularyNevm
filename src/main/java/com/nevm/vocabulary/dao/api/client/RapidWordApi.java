package com.nevm.vocabulary.dao.api.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nevm.vocabulary.config.properties.RapidProperties;
import com.nevm.vocabulary.dao.api.WordApi;
import com.nevm.vocabulary.dao.api.mapper.ClientMapper;
import com.nevm.vocabulary.service.model.WordDescription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RapidWordApi implements WordApi {
    private final RapidProperties rapidProperties;

    @Override
    public WordDescription getDescription(String word) {
        return ClientMapper.I.mapToWordDescription(
                    getWord(word),
                    getExamples(word)
            );
    }

    private String getWord(String word) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://wordsapiv1.p.rapidapi.com/words/" + word))
                .header("X-RapidAPI-Key", rapidProperties.getKey())
                .header("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        String response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public String getExamples(String word) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://wordsapiv1.p.rapidapi.com/words/" + word + "/examples"))
                .header("X-RapidAPI-Key", rapidProperties.getKey())
                .header("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        String response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }


}
