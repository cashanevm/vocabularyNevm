package com.nevm.vocabulary.dao.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.nevm.vocabulary.dao.persistence.entity.Word;

public interface WordRepository extends CrudRepository<Word, Long> {
    List<Word> findByStudied(Boolean bool);

    List<Word> findByStudiedAndOriginalNotIn(Boolean bool, List<String> originals);

    List<Word> findByWriteStudied(Boolean bool);
}
