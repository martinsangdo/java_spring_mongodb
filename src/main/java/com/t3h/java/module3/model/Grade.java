package com.t3h.java.module3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Grade {
    // private Date date;   //todo parse the data "$date": 1393804800000}
    private String grade;
    private Integer score;
}
