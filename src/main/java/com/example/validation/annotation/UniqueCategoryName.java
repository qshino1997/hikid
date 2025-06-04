package com.example.validation.annotation;

import com.example.validation.validator.UniqueCategoryNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE  })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueCategoryNameValidator.class)
public @interface UniqueCategoryName {
    String message() default "Tên danh mục đã tồn tại!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
