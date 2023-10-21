package com.nevm.vocabulary.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nevm.vocabulary.service.model.WordDescription;
import com.nevm.vocabulary.dao.persistence.entity.WordEntity;
import com.nevm.vocabulary.service.model.Word;

import lombok.extern.slf4j.Slf4j;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = ALWAYS)
@Slf4j
public abstract class ServiceMapper {

    /**
     * I.
     */
    public static final ServiceMapper I = Mappers.getMapper(ServiceMapper.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mapping(
            target = "description",
            source = "description",
            qualifiedByName = "mapToWordDescription"
    )
    @Mapping(
            target = "readDate",
            source = "date"
    )
    public abstract Word map(WordEntity map);

    @Mapping(
            target = "description",
            source = "description",
            qualifiedByName = "asString"
    )
    @Mapping(
            target = "wordDescription",
            ignore = true
    )
    @Mapping(
            target = "date",
            source = "readDate"
    )
    public abstract WordEntity map(Word word);

    @Named(value = "mapToWordDescription")
    public WordDescription mapToWordDescription(String asString) {
        try {
            return MAPPER.readValue(asString, WordDescription.class);
        } catch (JsonProcessingException e) {
            log.error("mapToWordDescription", e);

            return WordDescription.builder().build();
        }
    }

    @Named(value = "asString")
    public String asString(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("mapToWordDescription", e);

            return MAPPER.createObjectNode().asText();
        }
    }
}
