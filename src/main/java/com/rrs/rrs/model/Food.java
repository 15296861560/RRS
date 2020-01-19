package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Food {
    private Long foodId;
    private String foodName;
    private Double price;
    private String foodUrl;
    private String type;
}
