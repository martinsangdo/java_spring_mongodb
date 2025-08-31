package com.t3h.java.module3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Grade {
    private String grade;
    private Integer score;
}
