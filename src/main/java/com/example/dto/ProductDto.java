package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto extends GetCountryDto{
    private Integer product_id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
    private String name;

    @NotNull(message = "Khối lượng không được để trống")
    @Min(value = 0, message = "Khối lượng phải >= 0")
    private Integer product_weight;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải >= 0")
    private Integer price;

    @NotBlank(message = "Xuất xứ không được để trống")
    @Size(max = 100, message = "Xuất xứ tối đa 100 ký tự")
    private String made_in;

    @NotNull(message = "Tuổi bắt đầu không được để trống")
    @Min(value = 0, message = "Tuổi bắt đầu phải >= 0")
    private Integer appropriate_age_start;

    @NotNull(message = "Tuổi kết thúc không được để trống")
    @Min(value = 0, message = "Tuổi kết thúc phải >= 0")
    private Integer appropriate_age_end;

    @Size(max = 500, message = "Cảnh báo tối đa 500 ký tự")
    private String warning;

    @Size(max = 2000, message = "Hướng dẫn sử dụng tối đa 2000 ký tự")
    private String instructions;

    @Size(max = 1000, message = "Hướng dẫn bảo quản tối đa 1000 ký tự")
    private String storage_instructions;

    @NotNull(message = "Số lượng tồn không được để trống")
    @Min(value = 0, message = "Số lượng tồn phải >= 0")
    private Integer stock;

    @NotNull(message = "Phải chọn danh mục")
    private Integer category_id;

    private String category_name;

    @NotNull(message = "Phải chọn nhà sản xuất")
    private Integer manufacturer_id;

    private String manufacturer_name;

    private String url;

    public String getMadeInFull() {
        return made_in.isEmpty() ? "" : getCountryFull(made_in);
    }
}
