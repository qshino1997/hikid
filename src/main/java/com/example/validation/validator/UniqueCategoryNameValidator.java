package com.example.validation.validator;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.example.validation.annotation.UniqueCategoryName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueCategoryNameValidator implements ConstraintValidator<UniqueCategoryName, CategoryDto> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(UniqueCategoryName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(CategoryDto categoryDto, ConstraintValidatorContext constraintValidatorContext) {
        if (categoryDto.getName() == null || categoryDto.getName().trim().isEmpty()) {
            return true; // Trường hợp để trống sẽ được validator khác xử lý
        }

        // Nếu là edit
        if (categoryDto.getId() != null) {
            Category existing = categoryService.findByName(categoryDto.getName());
            if (existing == null || existing.getId().equals(categoryDto.getId())) {
                return true;
            }
        } else {
            // Nếu là tạo mới
            if (categoryService.findByName(categoryDto.getName()) == null) {
                return true;
            }
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                .addPropertyNode("name")
                .addConstraintViolation();

        return false;
    }
}
