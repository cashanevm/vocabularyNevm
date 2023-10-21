package com.nevm.vocabulary.dao.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.Date;

import com.nevm.vocabulary.dao.api.model.WordDescription;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String original;
    private String translation;
    private int combo = 0;
    private int writeCombo = 0;
    private boolean studied = false;
    private boolean writeStudied = false;
    private Date date = new Date();
    private Date writeDate = new Date();

    private String description;

    @Transient
    private WordDescription wordDescription;

    public Word(String original, String translation) {
        this.original = original;
        this.translation = translation;

    }
    public void setNewDate(){
        this.date = new Date();
    }
}
