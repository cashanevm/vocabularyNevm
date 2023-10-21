package com.nevm.vocabulary.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nevm.vocabulary.service.WordServiceImpl;
import com.nevm.vocabulary.service.model.Word;
import com.nevm.vocabulary.service.model.Words;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController {

    private final WordServiceImpl wordService;

    @GetMapping("/show")
    public String showWord(Model model) {
        Words wordList = wordService.getWords();

        model.addAttribute("letters", wordList.getLetters());
        model.addAttribute("words", wordList);

        return "info";
    }

    @GetMapping("/delete")
    public String deleteWord(@RequestParam(name = "id") Long id) {

        wordService.deleteWord(id);

        return "redirect:/word/show";
    }

    @GetMapping("/edit")
    public String editWordGet(@RequestParam(name = "id") Long id, Model model) {

        model.addAttribute("wordInfo", wordService.getWord(id));

        return "one";
    }

    @PostMapping("/edit")
    public String editWordPost(
    ) {
        return "redirect:/word/show";
    }

    @GetMapping("/add")
    public String addWordGet() {
        return "add";
    }

    @PostMapping("/add")
    public String addWordPost(
            @RequestParam(name = "original") String original,
            @RequestParam(name = "translation") String translation, Model model
    ) {
        Word word = wordService.createWord(original, translation);

        model.addAttribute("wordInfo", word);

        return "one";
    }
}
