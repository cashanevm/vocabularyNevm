package com.example.vocabularynevm.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.vocabularynevm.Entity.Word;
import com.example.vocabularynevm.Model.WordDescription;
import com.example.vocabularynevm.Repository.WordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordService {

    private final ObjectMapper objectMapper;

    private final WordRepository wordRepository;

    private final WordApi wordApi;

    private final TranslatorApi translatorApi;

    @PostConstruct
    public void init() {
//        for (Word w : wordRepository.findAll()) {
//            try {
//                WordDescription description = map(w).getWordDescription();
//
//                description.setExamples(wordApi.getExamples(w.getOriginal()));
//
//                System.out.printf("");
//
//                w.setDescription(objectMapper.writeValueAsString(description));
//
//                wordRepository.save(w);
//            } catch (Exception e) {
//                log.error("error", e);
//            }
//        }
    }

//    public static void main(String[] args) {
//        wordApi.getDescription("gathering");
//    }

    public Word createWord(Word word) {
        WordDescription description = wordApi.getDescription(word.getOriginal());

        if (!StringUtils.hasText(word.getTranslation())) {
            word.setTranslation(
                    translatorApi.translate(word.getOriginal())
            );
        }

        try {
            word.setDescription(objectMapper.writeValueAsString(description));
        } catch (JsonProcessingException e) {
            log.error("error", e);
        }

       return map(wordRepository.save(word));
    }

    public void updateWord(Word word) {
//        Word editedWord = wordRepository.findById(word.getId()).get();
//        editedWord.setCombo(word.getCombo());
//        editedWord.setOriginal(word.getOriginal());
//        editedWord.setTranslation(word.getTranslation());
//        editedWord.setStudied(word.isStudied());
        wordRepository.save(word);
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    public List<Word> getWords() {
        return ((ArrayList<Word>) wordRepository.findAll()).stream().map(this::map).collect(Collectors.toList());
    }

    public Word getWord(Long id) {
        return wordRepository.findById(id).map(this::map).get();
    }

    public ArrayList<Word> getReadGame() {
        Date newDate = new Date();
        ArrayList<Word> gameList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int random_number = (int) (Math.random() * getByStudiedAndOriginalNotIn(gameList).stream()
                    // .filter(x -> x.getDate().getDay() != newDate.getDay())
                    .count());


            gameList.add(
                    getByStudiedAndOriginalNotIn(gameList)
                            .stream()
//                            .filter(x -> x.getDate().getDay() != newDate.getDay())
                            .collect(Collectors.toList())
                            .get(random_number)
            );
        }

        return gameList;
    }

    private List<Word> getByStudiedAndOriginalNotIn(ArrayList<Word> gameList) {
        if (gameList.isEmpty()) {
            return wordRepository.findByStudied(
                    false
            ).stream().map(this::map).collect(Collectors.toList());
        } else {
            return wordRepository.findByStudiedAndOriginalNotIn(
                    false,
                    gameList.stream().map(Word::getOriginal).collect(Collectors.toList())
            ).stream().map(this::map).collect(Collectors.toList());
        }
    }

    public ArrayList<String> getLetters(ArrayList<Word> wordList) {
        ArrayList<String> letterList = new ArrayList<>();
        LinkedHashSet<String> letters = new LinkedHashSet<>();
        wordList.stream().forEach((x) -> {
            letters.add(x.getOriginal().split("")[0]);
        });
        letters.stream().forEach(x -> letterList.add(x));
        return letterList;
    }

    public Word getWriteGame() {
        List<Word> byWriteStudied = wordRepository.findByWriteStudied(Boolean.FALSE);

        int random_number = (int) (Math.random() * byWriteStudied.stream()
                // .filter(x -> x.getDate().getDay() != newDate.getDay())
                .count());

        return byWriteStudied
                .stream().map(this::map)
//                            .filter(x -> x.getDate().getDay() != newDate.getDay())
                .collect(Collectors.toList())
                .get(random_number);

    }

    private Word map(Word word) {
        try {
            word.setWordDescription(objectMapper.readValue(word.getDescription(), WordDescription.class));
        } catch (JsonProcessingException e) {
            log.error("map", e);
        }

        return word;
    }


}
