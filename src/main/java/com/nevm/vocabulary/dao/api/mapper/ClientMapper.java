package com.nevm.vocabulary.dao.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nevm.vocabulary.service.model.WordDefinition;
import com.nevm.vocabulary.service.model.WordDescription;

import lombok.extern.slf4j.Slf4j;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = ALWAYS)
@Slf4j
public abstract class ClientMapper {

    /**
     * I.
     */
    public static final ClientMapper I = Mappers.getMapper(ClientMapper.class);

    private static final ObjectMapper MAPPER = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    public WordDescription mapToWordDescription(String wordResponse, String exampleResponse) {
        return WordDescription.builder()
                .all(wordResponse)
                .pronunciation(getPronunciation(wordResponse))
                .definitions(
                        getDefinitions(wordResponse)
                )
                .examples(getExamples(exampleResponse))
                .build();
    }

    private String getPronunciation(String all) {
        try {
            JsonNode jsonNode = MAPPER.readTree(all);

            JsonNode pronunciation = jsonNode.findValue("pronunciation");

            if (pronunciation == null) {
                return null;
            } else if (pronunciation.isTextual()) {
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

    private List<WordDefinition> getDefinitions(String response) {
        try {
            JsonNode jsonNode = MAPPER.readTree(response);

            JsonNode definitions = jsonNode.findValue("results");

            if (definitions == null) {
                definitions = jsonNode.findValue("definitions");
            }

            if (definitions != null && definitions.isArray()) {
                ArrayNode arrayNode = (ArrayNode) definitions;

                ArrayList<WordDefinition> result = new ArrayList<>();

                for (JsonNode node : arrayNode) {
                    result.add(MAPPER.readValue(MAPPER.writeValueAsString(node), WordDefinition.class));
                }

                return result;
            } else {
                log.error("definitions :" + definitions);
                log.error("definitions r:" + response);

                return null;
            }

        } catch (JsonProcessingException e) {
            log.error("Definitions", e);

            return null;
        }
    }

    public List<String> getExamples(String response) {
        JsonNode jsonNode = null;
        try {
            jsonNode = MAPPER.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        jsonNode = jsonNode.findValue("examples");

        ArrayList<String> strings = new ArrayList<>();


        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode node : ((ArrayNode) jsonNode)) {
                strings.add(node.asText());
            }
        } else {
            throw new RuntimeException();
        }

        return strings;
    }
}
