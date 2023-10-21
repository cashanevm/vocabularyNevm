package com.nevm.vocabulary.service.api;

import java.util.List;

import com.nevm.vocabulary.service.model.Word;
import com.nevm.vocabulary.service.model.Words;

public interface WordService {
    Word createWord(String original, String translation);

    void updateWord(Word word);

    void deleteWord(Long id);

    Words getWords();

    Word getWord(Long id);

    Words getReadGame();

    Word getWriteGame();
}
