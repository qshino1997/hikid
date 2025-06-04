package com.example.validation.validator;

import com.example.dao.ProductDAO;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.service.ProductService;
import com.example.validation.annotation.UniqueProductName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueProductNameValidator implements ConstraintValidator<UniqueProductName, ProductDto> {

    @Autowired
    private ProductService productService;

    @Override
    public void initialize(UniqueProductName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(ProductDto p, ConstraintValidatorContext constraintValidatorContext) {
        if (p.getName() == null || p.getName().trim().isEmpty() ) {
            return true;
        }

        Product existing = productService.getByName(p.getName().trim());

        if (existing == null) {
            return true; // Tên chưa tồn tại => OK
        }

        // Nếu đang sửa và tên trùng với chính nó => OK
        if (p.getProduct_id() != null && existing.getProduct_id().equals(p.getProduct_id())) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                .addPropertyNode("name")
                .addConstraintViolation();
        return false;
    }
}
