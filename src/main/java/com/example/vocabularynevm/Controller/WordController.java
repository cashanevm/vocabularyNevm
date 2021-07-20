package com.example.vocabularynevm.Controller;

import com.example.vocabularynevm.Entity.Word;
import com.example.vocabularynevm.Service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;
    @GetMapping("/show")
    public String showWord(Model model){
        ArrayList<Word> wordList = (ArrayList<Word>) wordService.getWords().stream().sorted(Comparator.comparing(x -> x.getOriginal())).collect(Collectors.toList());
        model.addAttribute("letters", wordService.getLetters(wordList));
        model.addAttribute("words",wordList);
        return "info";
    }
    @GetMapping("/delete")
    public String deleteWord(@RequestParam(name ="id")  String id){
        wordService.deleteWord(Long.parseLong(id));
        return "redirect:/word/show";
    }
    @GetMapping("/edit")
    public String editWordGet(@RequestParam(name ="id")  String id,Model model){
        model.addAttribute("wordInfo", wordService.getWord(Long.parseLong(id)));
        return "edit";
    }
    @PostMapping("/edit")
    public String editWordPost(@RequestParam(name ="id")  String id,@RequestParam(name ="original")  String original,@RequestParam(name ="translation")  String translation,@RequestParam(name ="combo")  String combo,@RequestParam(name ="studied")  String studied){
        Word word = wordService.getWord(Long.parseLong(id));
        word.setStudied(Boolean.parseBoolean(studied));
        word.setTranslation(translation);
        word.setCombo(Integer.parseInt(combo));
        word.setOriginal(original);
        wordService.updateWord(word);
        return "redirect:/word/show";
    }
    @GetMapping("/add")
    public String addWordGet(){
        return "add";
    }
    @PostMapping("/add")
    public String addWordPost(@RequestParam(name ="original")  String original,@RequestParam(name ="translation")  String translation){
        wordService.createWord(new Word(original,translation));
        return "redirect:/word/show";
    }


}
