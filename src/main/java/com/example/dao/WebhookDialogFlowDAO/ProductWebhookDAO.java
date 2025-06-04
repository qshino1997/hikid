package com.example.dao.WebhookDialogFlowDAO;

import com.example.dto.ProductDto;
import com.example.entity.Manufacturer;
import com.example.entity.Product;

import java.util.List;

public interface ProductWebhookDAO {
    List<ProductDto> findManufacturerName(String brand);

    ProductDto findByName(String name);
}
