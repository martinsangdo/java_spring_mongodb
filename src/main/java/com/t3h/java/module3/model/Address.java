package com.t3h.java.module3.model;

import java.util.List;
import lombok.Data;

@Data
public class Address {
    private String building;
    private List<Double> coord;
    private String street;
    private String zipcode;
}