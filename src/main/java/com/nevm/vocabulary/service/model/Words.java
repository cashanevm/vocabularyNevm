package com.nevm.vocabulary.service.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Words extends ArrayList<Word> {
    public Words() {
        super();
    }

    public List<String> getLetters() {
        LinkedHashSet<String> letters = new LinkedHashSet<>();

        this.forEach((x) -> letters.add(x.getOriginal().split("")[0]));

        return new ArrayList<>(letters);
    }
}
