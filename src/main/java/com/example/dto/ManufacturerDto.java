package com.example.dto;

import com.example.validation.annotation.UniqueManufacturerName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@UniqueManufacturerName
public class ManufacturerDto {
    private Integer id;

    @NotBlank(message = "Tên nhà sản xuất không được để trống")
    @Size(max = 255, message = "Tên tối đa 255 ký tự")
    private String name;

    @NotBlank(message = "Quốc gia không được để trống")
    @Size(max = 100, message = "Tên quốc gia tối đa 100 ký tự")
    private String country;

    @NotBlank(message = "Website không được để trống")
    @Size(max = 255, message = "Đường dẫn website tối đa 255 ký tự")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Website phải bắt đầu với http:// hoặc https://"
    )
    private String website;

    @Min(value = 0, message = "Followers phải lớn hơn hoặc bằng 0")
    private Integer followers_count;

    @Min(value = 0, message = "Satisfaction level phải từ 0 đến 100")
    @Max(value = 100, message = "Satisfaction level phải từ 0 đến 100")
    private Byte satisfaction_level;

    @DecimalMin(value = "0.0", inclusive = false, message = "Rating phải lớn hơn 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating phải nhỏ hơn hoặc bằng 5.0")
    private BigDecimal rating;

    @Min(value = 0, message = "Number of reviews phải lớn hơn hoặc bằng 0")
    private Integer number_of_reviews;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
