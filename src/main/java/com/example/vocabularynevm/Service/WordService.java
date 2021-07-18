package com.example.vocabularynevm.Service;

import com.example.vocabularynevm.Entity.Word;
import com.example.vocabularynevm.Repository.WordRepository;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;
    public WordService( WordRepository wordRepository){
        this.wordRepository = wordRepository;

    }

    public void createWord(Word word){
        wordRepository.save(word);
    }
    public void updateWord(Word word){
        Word editedWord = wordRepository.findById(word.getId()).get();
        editedWord.setCombo(word.getCombo());
        editedWord.setOriginal(word.getOriginal());
        editedWord.setTranslation(word.getTranslation());
        editedWord.setStudied(word.isStudied());
        wordRepository.save(editedWord);
    }
    public void deleteWord(Long id){
        wordRepository.deleteById(id);
    }
    public ArrayList<Word> getWords(){
        return (ArrayList<Word>) wordRepository.findAll();
    }
    public Word getWord(Long id){
        return wordRepository.findById(id).get();
    }
    public ArrayList<Word> getGame(){
        Date newDate = new Date();
        ArrayList<Word> gameList = new ArrayList<>();
         for(int i =0 ; i<4 ; i++){

             int random_number = 0 + (int) (Math.random() * wordRepository.findByStudied(false).stream().filter(x->x.getDate().getDay() != newDate.getDay()).count());
             gameList.add(wordRepository.findByStudied(false).stream().filter(x->x.getDate().getDay() == newDate.getDay()).collect(Collectors.toList()).get(random_number));
         }
        return gameList;
    }

}
