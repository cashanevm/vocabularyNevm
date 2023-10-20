package com.example.vocabularynevm.Controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.vocabularynevm.Entity.Word;
import com.example.vocabularynevm.Service.WordService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/game/write")
@Controller
@RequiredArgsConstructor
public class WriteGameController {

    private final WordService wordService;

    @GetMapping("/")
    public String writeGame(Model model) {

        Word word = wordService.getWriteGame();
        model.addAttribute("word", word);

        return "write-game";
    }

    @GetMapping("/get")
    public String getResult(
            Model model,
            @RequestParam(name = "id") String id,
            @RequestParam(name = "right") String right,
            @RequestParam(name = "type") String type
    ) {
        Word word = wordService.getWord(Long.parseLong(id));
        model.addAttribute("word", word);
        if (right.equals(type)) {
            word.setWriteCombo(word.getWriteCombo() + 1);
            if (word.getWriteCombo() > 10) {
                word.setWriteStudied(true);
            }
            word.setWriteDate(new Date());
            model.addAttribute("message", "You are good '" + type + "'! everything is right!");
            model.addAttribute("class", "alert alert-success");
        } else {
            word.setWriteCombo(0);
            model.addAttribute("message", "Not right '" + type + "'! next time check better!");
            model.addAttribute("class", "alert alert-danger");
        }
        wordService.updateWord(word);
        return "write-right";
    }


}