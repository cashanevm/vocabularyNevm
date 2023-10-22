package com.nevm.vocabulary.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nevm.vocabulary.dao.api.TranslatorApi;
import com.nevm.vocabulary.dao.api.WordApi;
import com.nevm.vocabulary.dao.api.client.RapidTranslatorApi;
import com.nevm.vocabulary.dao.api.client.RapidWordApi;
import com.nevm.vocabulary.dao.persistence.entity.WordEntity;
import com.nevm.vocabulary.dao.persistence.repository.WordRepository;
import com.nevm.vocabulary.service.api.WordService;
import com.nevm.vocabulary.service.mapper.ServiceMapper;
import com.nevm.vocabulary.service.model.Word;
import com.nevm.vocabulary.service.model.Words;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toCollection;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    private final WordApi wordApi;

    private final TranslatorApi translatorApi;

    @Override
    public Word createWord(
            final String original,
            final String translation
    ) {
        Word word = Word.builder()
                .original(original)
                .translation(translationOf(original, translation))
                .description(wordApi.getDescription(original))
                .build();

        return ServiceMapper.I.map(
                wordRepository.save(ServiceMapper.I.map(word))
        );
    }

    @Override
    public void updateWord(Word word) {
        wordRepository.save(ServiceMapper.I.map(word));
    }

    @Override
    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    @Override
    public Words getWords() {
        return StreamSupport
                .stream(wordRepository.findAll().spliterator(), false)
                .map(ServiceMapper.I::map)
                .sorted(Comparator.comparing(Word::getOriginal))
                .collect(toCollection(Words::new));
    }

    @Override
    public Word getWord(Long id) {
        return wordRepository.findById(id).map(ServiceMapper.I::map)
                .orElse(Word.builder().build());
    }

    @Override
    public Words getReadGame() {
        ArrayList<WordEntity> gameList = new ArrayList<>();

        Date newDate = new Date();
        for (int i = 0; i < 4; i++) {
            List<WordEntity> studiedAndOriginalNotIn = getByStudiedAndOriginalNotIn(gameList);

            int randomNumber = (int) (Math.random() * studiedAndOriginalNotIn.stream()
                    .filter(x -> x.getDate().getDay() != newDate.getDay())
                    .count()
            );

            gameList.add(
                    studiedAndOriginalNotIn.stream()
                            .filter(x -> x.getDate().getDay() != newDate.getDay())
                            .collect(Collectors.toList())
                            .get(randomNumber)
            );
        }

        return gameList.stream().map(ServiceMapper.I::map)
                .collect(toCollection(Words::new));
    }

    @Override
    public Word getWriteGame() {
        List<WordEntity> byWriteStudied = wordRepository.findByWriteStudied(Boolean.FALSE);

        int randomNumber = (int) (Math.random() * byWriteStudied.stream()
                 .filter(x -> x.getWriteDate().getDay() != new Date().getDay())
                .count());

        return ServiceMapper.I.map(byWriteStudied.stream()
                .filter(x -> x.getWriteDate().getDay() != new Date().getDay())
                .collect(Collectors.toList()).get(randomNumber));

    }

    private List<WordEntity> getByStudiedAndOriginalNotIn(ArrayList<WordEntity> gameList) {
        if (gameList.isEmpty()) {
            return new ArrayList<>(wordRepository.findByStudied(
                    false
            ));
        } else {
            return new ArrayList<>(wordRepository.findByStudiedAndOriginalNotIn(
                    false,
                    gameList.stream().map(WordEntity::getOriginal).collect(Collectors.toList())
            ));
        }
    }

    private String translationOf(
            final String original,
            final String translation
    ) {
        if (!StringUtils.hasText(translation)) {
            return translatorApi.translate(original);

        } else {
            return translation;
        }
    }
}
