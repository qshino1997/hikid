package com.example.service.WebhookDialogFlowService.impl;

import com.example.dao.ProductDAO;
import com.example.dao.WebhookDialogFlowDAO.ProductWebhookDAO;
import com.example.dto.ProductDto;
import com.example.service.Cart.CartService;
import com.example.service.WebhookDialogFlowService.ProductWebhookService;
import com.example.util.CartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductWebhookServiceImpl implements ProductWebhookService {

    @Autowired
    private CartUtil cartUtil;
    @Autowired
    private ProductWebhookDAO productWebhookDAO;
    @Autowired
    private ProductDAO productDAO;
    @Override
    public List<ProductDto> findManufacturerName(String brand) {
        return productWebhookDAO.findManufacturerName(brand);
    }

    @Override
    public ProductDto findByName(String name) {
        return productWebhookDAO.findByName(name);
    }

    @Override
    public void addToCart(Integer productId) {
        try{
            CartService cartService = cartUtil.currentCart();
            cartService.addItem(productDAO.getById(productId),1);
        } catch (Exception e) {
        }
    }

    @Override
    public Integer getQualityCart() {
        return cartUtil.currentCart().getTotalQuantity();
    }
}
