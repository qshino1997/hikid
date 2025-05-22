package com.example.service.impl;

import com.example.dto.CartItem;
import com.example.dto.ProductDto;
import com.example.service.CartService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartServiceImpl implements CartService {
    // key = productId
    private Map<Integer, CartItem> items = new LinkedHashMap<>();

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void addItem(ProductDto p, int qty) {
        CartItem item = items.get(p.getProduct_id());
        if (item == null) {
            items.put(p.getProduct_id(), new CartItem(p, qty));
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }
    }

    @Override
    public void updateItem(int productId, int qty) {
        CartItem item = items.get(productId);
        if (item != null) {
            if (qty <= 0) items.remove(productId);
            else item.setQuantity(qty);
        }
    }

    @Override
    public void removeItem(int productId) {
        items.remove(productId);
    }

    @Override
    public Collection<CartItem> getItems() {
        return items.values();
    }

    @Override
    public long getTotal() {
        return items.values().stream()
                .mapToLong(CartItem::getSubTotal)
                .sum();
    }

    public int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
