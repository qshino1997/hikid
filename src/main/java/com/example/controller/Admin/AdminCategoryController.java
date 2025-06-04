package com.example.controller.Admin;

import com.example.dto.CategoryDto;
import com.example.dto.Form.CategoryOption;
import com.example.entity.Category;
import com.example.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public String categoriesPage() {
        return "admin.category";
    }

    @GetMapping("/ajaxCategory")
    public String ajaxCategory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        long total = categoryService.countCategories(keyword);
        model.addAttribute("categories", categoryService.getAllCategories(page, size, keyword));
        model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
        model.addAttribute("page", page);
        model.addAttribute("pages", (int)Math.ceil((double) total / size));
        return "admin/category-table";
    }

    // Hiển thị form Thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
        model.addAttribute("parents", parents);
        model.addAttribute("mode", "create");
        return "admin/category-details";
    }

    // Xử lý POST Thêm mới
    @PostMapping("/create")
    public String createCategory(
            @Valid @ModelAttribute("category") CategoryDto categoryDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            model.addAttribute("category", categoryDto);
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            model.addAttribute("mode", "create");
            return "admin/category-details";
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        if(categoryDto.getCategoryParentId() != null){
            category.setParent(categoryService.findById(categoryDto.getCategoryParentId()));
        }

        categoryService.save(category);
        redirectAttrs.addFlashAttribute("success", "Tạo danh mục mới thành công!");
        return "redirect:/admin/category";
    }

    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttrs) {
        try {
            Category category = categoryService.findById(id);
            if(!category.getProducts().isEmpty() || !category.getSubCategories().isEmpty()){
                // Nếu còn ràng buộc FK (còn con hoặc sản phẩm), bắt ngoại lệ
                redirectAttrs.addFlashAttribute("failed",
                        "Không thể xóa: danh mục vẫn đang có danh mục con hoặc sản phẩm.");
                return "redirect:/admin/category";
            } else {
                categoryService.deleteById(id);
                redirectAttrs.addFlashAttribute("success", "Xóa danh mục thành công.");
            }

        } catch (DataIntegrityViolationException ex) {
            // Nếu còn ràng buộc FK (còn con hoặc sản phẩm), bắt ngoại lệ
            redirectAttrs.addFlashAttribute("failed",
                    "Không thể xóa: danh mục vẫn đang có danh mục con hoặc sản phẩm.");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/{id}/update")
    public String showEditForm(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes ra) {

        Category c = categoryService.findById(id);
        if (c == null) {
            ra.addFlashAttribute("failed", "Không tìm thấy danh mục");
            return "redirect:/admin/category";
        }
        CategoryDto categoryDto = modelMapper.map(c, CategoryDto.class);

        model.addAttribute("category", categoryDto);
        model.addAttribute("mode", "edit");
        List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
        model.addAttribute("parents", parents);
        return "admin/category-details";
    }


    @PostMapping("/update")
    public String updateCategory(
            @Valid @ModelAttribute("category") CategoryDto categoryDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra
    ) {

        // Nếu đã có lỗi @Valid (ví dụ @NotBlank, @UniqueCategoryName)
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            return "admin/category-details";
        }

        // Lấy category cũ
        Category category = categoryService.findById(categoryDto.getId());

        // Kiểm tra subCategories trước: nếu có con, lỗi luôn, không xét tiếp
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            bindingResult.rejectValue(
                    "name",
                    "category.hasChildren",
                    "Không thể sửa vì danh mục này đang có danh mục con.");
            model.addAttribute("mode", "edit");
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            return "admin/category-details";
        }

        // Kiểm tra parent trùng chính nó
        if(Objects.equals(categoryDto.getId(), categoryDto.getCategoryParentId())){
            bindingResult.rejectValue(
                    "categoryParentId",
                    "category.parentSelf",
                    "Thao tác sai, danh mục cha không được trùng với chính nó"
            );
            model.addAttribute("mode", "edit");
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            return "admin/category-details";
        }

        Category parent = null;
        // Cập nhật parent (null nếu không chọn)
        if (categoryDto.getCategoryParentId() != null ) {
            parent = categoryService.findById(categoryDto.getCategoryParentId());
            if(parent == null){
                bindingResult.rejectValue(
                        "categoryParentId",
                        "category.parentMissing",
                        "Không tìm thấy danh mục cha"
                );
                model.addAttribute("mode", "edit");
                model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
                return "admin/category-details";
            }
        }

        category.setName(categoryDto.getName().trim());
        category.setParent(parent);

        // Lưu vào CSDL
        categoryService.save(category);

        // Trả về success
        ra.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        return "redirect:/admin/category";
    }
}