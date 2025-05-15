package com.example.dto;

import com.example.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private ProductDto product;
    private int quantity;

    public long getSubTotal() {
        return product.getPrice() * quantity;
    }
}
