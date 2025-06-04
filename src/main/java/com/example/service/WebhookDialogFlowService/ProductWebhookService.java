package com.example.service.WebhookDialogFlowService;

import com.example.dto.ProductDto;

import java.util.List;

public interface ProductWebhookService {
    List<ProductDto> findManufacturerName(String brand);
    ProductDto findByName(String name);
    void addToCart(Integer productId);
    Integer getQualityCart();
}
