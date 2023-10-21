package com.nevm.vocabulary.web;

import java.util.Collections;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nevm.vocabulary.service.WordServiceImpl;
import com.nevm.vocabulary.service.model.Word;
import com.nevm.vocabulary.service.model.Words;

import lombok.RequiredArgsConstructor;

@RequestMapping("/game/read")
@Controller
@RequiredArgsConstructor
public class ReadGameController {

    private final WordServiceImpl wordService;

    @GetMapping("/")
    public String readGame(Model model) {

        Words words = wordService.getReadGame();

        model.addAttribute("word", words.get(2));
        Collections.shuffle(words);
        model.addAttribute("words", words);

        return "read-game";
    }

    @GetMapping("/get")
    public String getResult(
            Model model,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "right") String right,
            @RequestParam(name = "type") String type
    ) {
        Word word = wordService.getWord(id);

        model.addAttribute("word", word);
        if (right.equals(type)) {
            word.setCombo(word.getCombo() + 1);
            if (word.getCombo() > 4) {
                word.setStudied(true);
            }
            word.setReadDate(new Date());
            model.addAttribute("message", "You are good! everything is right!");
            model.addAttribute("class", "alert alert-success");

        } else {
            word.setCombo(0);
            model.addAttribute("message", "Not right! next time check better!");
            model.addAttribute("class", "alert alert-danger");

        }

        wordService.updateWord(word);

        return "read-right";
    }
}
