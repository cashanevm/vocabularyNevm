package com.example.vocabularynevm.Controller;

import com.example.vocabularynevm.Entity.Word;
import com.example.vocabularynevm.Service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;

@RequestMapping("/game")
@Controller
public class GameController {
    @Autowired
    private WordService wordService;

    @GetMapping("/")
    public String game(Model model){

        ArrayList<Word> words = wordService.getGame();
        model.addAttribute("word",words.get(2));
        Collections.shuffle(words);
        model.addAttribute("words",words);
        return "game";
    }
    @GetMapping("/get")
    public String getResult(Model model,@RequestParam(name ="id")  String id,@RequestParam(name ="right")  String right,@RequestParam(name ="type")  String type){
        Word word = wordService.getWord(Long.parseLong(id));
        model.addAttribute("word", word);
        if(right.equals(type)){
            word.setCombo(word.getCombo()+1);
            if(word.getCombo()>4){
                word.setStudied(true);
            }
            word.setNewDate();
            model.addAttribute("message", "You are good! everything is right!");
            model.addAttribute("class", "alert alert-success");

        }else {
            word.setCombo(0);
            model.addAttribute("message", "Not right! next time check better!");
            model.addAttribute("class", "alert alert-danger");

        }
        wordService.updateWord(word);
        return "right";
    }



}
