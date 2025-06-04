package com.example.dto.Form;

import com.example.dto.HasEmail;
import com.example.validation.annotation.UniqueEmailUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UniqueEmailUser
public class RegisterForm implements HasEmail {

    // Basic User Info
    private Integer user_id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 50, message = "Họ tên tối đa 50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 5, message = "Mật khẩu tối thiểu 5 ký tự")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Integer getUser_id() {
        return this.user_id;
    }
}
