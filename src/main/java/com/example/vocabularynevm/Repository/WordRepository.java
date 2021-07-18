package com.example.vocabularynevm.Repository;

import com.example.vocabularynevm.Entity.Word;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WordRepository extends CrudRepository<Word, Long> {
    public List<Word> findByStudied(Boolean bool);
}
