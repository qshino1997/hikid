package com.example.util;

import com.example.service.Cart.CartService;
import com.example.service.Cart.SessionCartService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CartUtil {
    private final SecurityUtil securityUtil;
    private final SessionCartService sessionCartService;
    private final ObjectProvider<CartService> dbCartProvider;

    public CartUtil(SecurityUtil securityUtil,
                     @Qualifier("sessionCartService") SessionCartService sessionCartService,
                     @Qualifier("databaseCartService") ObjectProvider<CartService> dbCartProvider) {
        this.securityUtil = securityUtil;
        this.sessionCartService = sessionCartService;
        this.dbCartProvider = dbCartProvider;
    }

    /**
     * Trả về CartService phù hợp:
     * - nếu user đã login (securityUtil.getCurrentUserId() != null): lấy DB-cart
     * - nếu chưa login: trả sessionCartService
     */
    public CartService currentCart() {
        Integer userId = securityUtil.getCurrentId();
        if (userId != null) {
            CartService dbCart = dbCartProvider.getObject();
            dbCart.initForUser(userId);
            return dbCart;
        }
        return sessionCartService;
    }

}
