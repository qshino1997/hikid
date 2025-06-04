package com.example.dto;

import com.example.validation.annotation.UniqueCategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UniqueCategoryName
public class CategoryDto {
    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục tối đa 100 ký tự")
    private String name;

    private Integer categoryParentId;

    // Nếu cần hiển thị tên parent trong form edit, bạn có thể thêm:
    private String categoryParentName;

}
