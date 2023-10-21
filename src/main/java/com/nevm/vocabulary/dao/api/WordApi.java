package com.nevm.vocabulary.dao.api;

import com.nevm.vocabulary.service.model.WordDescription;

public interface WordApi {
    WordDescription getDescription(String word);
}
