package com.nevm.vocabulary.dao.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.nevm.vocabulary.dao.persistence.entity.WordEntity;

public interface WordRepository extends CrudRepository<WordEntity, Long> {
    List<WordEntity> findByStudied(Boolean bool);

    List<WordEntity> findByStudiedAndOriginalNotIn(Boolean bool, List<String> originals);

    List<WordEntity> findByWriteStudied(Boolean bool);
}
