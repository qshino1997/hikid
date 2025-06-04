package com.example.validation.validator;

import com.example.dto.ManufacturerDto;
import com.example.entity.Manufacturer;
import com.example.service.ManufacturerService;
import com.example.validation.annotation.UniqueManufacturerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueManufacturerNameValidator implements ConstraintValidator<UniqueManufacturerName, ManufacturerDto> {
    @Autowired
    private ManufacturerService manufacturerService;

    @Override
    public void initialize(UniqueManufacturerName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(ManufacturerDto manufacturer, ConstraintValidatorContext constraintValidatorContext) {
        if (manufacturer.getName() == null || manufacturer.getName().trim().isEmpty()) {
            return true; // Trường hợp để trống, validator khác xử lý
        }

        // Nếu là edit
        if (manufacturer.getId() != null) {
            Manufacturer existing = manufacturerService.findByName(manufacturer.getName());
            if (existing == null || existing.getId().equals(manufacturer.getId())) {
                return true;
            }
        } else {
            // Nếu là thêm mới
            if (manufacturerService.findByName(manufacturer.getName()) == null) {
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
