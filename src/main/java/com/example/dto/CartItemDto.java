package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private ProductDto product;
    private int quantity;

    public long getSubTotal() {
        return (long) product.getPrice() * quantity;
    }
}
