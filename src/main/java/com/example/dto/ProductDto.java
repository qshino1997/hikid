package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto extends GetCountryDto{
    private Integer product_id;
    private String name;
    private Integer product_weight;
    private Integer price;
    private String made_in;
    private Integer appropriate_age_start;
    private Integer appropriate_age_end;
    private String warning;
    private String instructions;
    private String storage_instructions;
    private Integer stock;
    private Integer manufacturerId;
    private String manufacturerName;
    private String manufacturercountry;

    public String getMadeInFull() {
        return made_in.isEmpty() ? "" : getCountryFull(made_in);
    }
}
