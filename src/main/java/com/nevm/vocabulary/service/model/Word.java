package com.nevm.vocabulary.service.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Word {
    private long id;

    private String original;

    private String translation;

    private Integer combo;

    private Integer writeCombo;

    private Boolean studied;

    private Boolean writeStudied;

    private Date readDate;

    private Date writeDate;

    private WordDescription description;
}
