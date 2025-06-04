package com.example.service.Cart;

import com.example.dto.CartItemDto;
import com.example.dto.ProductDto;
import com.example.entity.CartItem;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Service("sessionCartService")
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@Primary
public class SessionCartService implements CartService {
    // key = productId
    private Map<Integer, CartItemDto> items = new LinkedHashMap<>();

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void initForUser(Integer userId) {

    }

    @Override
    public void addItem(ProductDto p, int qty) {
        CartItemDto item = items.get(p.getProduct_id());
        if (item == null) {
            items.put(p.getProduct_id(), new CartItemDto(p, qty));
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }
    }

    @Override
    public void updateItem(int productId, int qty) {
        CartItemDto item = items.get(productId);
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
    public Collection<CartItemDto> getItems() {
        return items.values();
    }

    @Override
    public long getTotal() {
        return items.values().stream()
                .mapToLong(CartItemDto::getSubTotal)
                .sum();
    }

    @Override
    public int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
