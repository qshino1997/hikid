package com.example.dto.Form;

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
public class OrderForm {
    // Basic User Info
    private Integer user_id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 50, message = "Họ tên tối đa 50 ký tự")
    private String username;

    @Email(message = "Email không hợp lệ")
    private String email_v2;

    @Size(min = 9, max = 13,
            message = "Số điện thoại không hợp lệ ")
    private String phone;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

}
