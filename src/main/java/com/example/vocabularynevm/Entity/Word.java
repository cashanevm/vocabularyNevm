package com.example.vocabularynevm.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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
    private boolean studied = false;
    private Date date = new Date();

    public Word(String original, String translation) {
        this.original = original;
        this.translation = translation;

    }
    public void setNewDate(){
        this.date = new Date();
    }

}
