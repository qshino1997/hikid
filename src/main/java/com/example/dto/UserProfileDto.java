package com.example.dto;

import com.example.validation.annotation.UniqueEmailUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@UniqueEmailUser
public class UserProfileDto  implements  HasEmail{

    // Basic User Info
    private Integer user_id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 50, message = "Họ tên tối đa 50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    // Profile Info
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;

    @Size(min = 9, max = 13,
            message = "Số điện thoại không hợp lệ ")
    private String phone;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Integer getUser_id() {
        return this.user_id;
    }
}
