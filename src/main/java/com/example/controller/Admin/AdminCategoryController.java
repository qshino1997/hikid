package com.example.controller.Admin;

import com.example.dto.CategoryOption;
import com.example.entity.Category;
import com.example.service.CategoryService;
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
        model.addAttribute("category", new Category());
        List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
        model.addAttribute("parents", parents);
        model.addAttribute("mode", "create");
        return "admin/category-details";
    }

    // Xử lý POST Thêm mới
    @PostMapping("/create")
    public String createCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            model.addAttribute("failed", "Tạo danh mục không thành công");
            model.addAttribute("mode", "create");
            return "admin/category-details";
        }

        if(categoryService.findByName(category.getName()) != null){
            model.addAttribute("category", category);
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            model.addAttribute("failed", "Danh mục này đã tồn tại");
            model.addAttribute("mode", "create");
            return "admin/category-details";
        }

        if(category.getParent().getId() == null){
            category.setParent(null);
        }

        category.setProducts(null);

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
        model.addAttribute("category", c);
        model.addAttribute("mode", "edit");
        List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
        model.addAttribute("parents", parents);
        return "admin/category-details";
    }


    @PostMapping("/update")
    public String updateCategory(
            @ModelAttribute("category") Category _category,
            Model model,
            RedirectAttributes ra
    ) {
        // 1. Lấy category cũ
        Category category = categoryService.findById(_category.getId());

        if(category == null || _category.getName().isEmpty()){
            model.addAttribute("mode", "edit");
            List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
            model.addAttribute("parents", parents);
            model.addAttribute("failed", "Cập nhật danh mục thất bại");
            return "admin/category-details";
        }

        if(categoryService.findByName(category.getName()) != null){
            model.addAttribute("category", category);
            model.addAttribute("parents", categoryService.findParentsUpToLevel(2));
            model.addAttribute("failed", "Danh mục này đã tồn tại");
            model.addAttribute("mode", "edit");
            return "admin/category-details";
        }

        // 2. Cập nhật tên
        category.setName(_category.getName().trim());

        // 3. Cập nhật parent (null nếu không chọn)
        if (_category.getParent().getId() != null ) {
            if (category.getSubCategories() != null && category.getSubCategories().size() != 0) {
                model.addAttribute("mode", "edit");
                List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
                model.addAttribute("parents", parents);
                model.addAttribute("failed", "Không thể sửa vì danh mục này đang có danh mục con.");
                return "admin/category-details";
            }

            Category parent = categoryService.findById(_category.getParent().getId());

            if(parent == null){
                model.addAttribute("mode", "edit");
                List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
                model.addAttribute("parents", parents);
                model.addAttribute("failed", "Không tìm thấy danh muc cha");
                return "admin/category-details";
            }

            if(category.getParent() == null){
                category.setParent(parent);
            }

            if(Objects.equals(parent.getId(), _category.getId())){
                model.addAttribute("mode", "edit");
                List<CategoryOption> parents = categoryService.findParentsUpToLevel(2);
                model.addAttribute("parents", parents);
                model.addAttribute("failed", "Thao tác sai, danh mục bị trùng");
                return "admin/category-details";
            }

            if(category.getParent() != null
                    && (!Objects.equals(parent.getId(), _category.getId()))){
                category.setParent(parent);
            }
        }
         else {
            category.setParent(null);
        }
        // 4. Lưu vào CSDL
        categoryService.save(category);

        // 5. Trả về success
        ra.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        return "redirect:/admin/category";
    }
}