package com.nevm.vocabulary.dao.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "word")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String original;

    private String translation;

    private int combo;

    private int writeCombo;

    private boolean studied;

    private boolean writeStudied;

    private Date date = new Date();

    private Date writeDate = new Date();

    private String description;
}
