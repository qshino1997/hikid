package com.example.validation.validator;

import com.example.dao.UserDAO;
import com.example.dto.HasEmail;
import com.example.dto.UserProfileDto;
import com.example.entity.User;
import com.example.validation.annotation.UniqueEmailUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailUser, HasEmail> {
    @Autowired
    private UserDAO userDAO;

    @Override
    public void initialize(UniqueEmailUser constraintAnnotation) {
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(HasEmail dto, ConstraintValidatorContext context) {
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            // Các annotation @NotEmpty, @Email sẽ tự xử lý
            return true;
        }
        User existing = userDAO.findByEmail(dto.getEmail());
        if (existing == null) {
            // email chưa có ai dùng → hợp lệ
            return true;
        }

        Integer editingId = dto.getUser_id();
        if (editingId != null && editingId.equals(existing.getId())) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("email")
                .addConstraintViolation();
        return false;
    }
}
