package com.example.validation.annotation;

import com.example.validation.validator.UniqueManufacturerNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE  })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueManufacturerNameValidator.class)
public @interface UniqueManufacturerName {
    String message() default "Tên nhà sản xuất đã tồn tại!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
