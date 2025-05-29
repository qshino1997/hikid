package com.example.controller.Admin;

import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Manufacturer;
import com.example.entity.Product;
import com.example.service.CategoryService;
import com.example.service.ManufacturerService;
import com.example.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public String productsPage(){
        return "admin.product";
    }

    @GetMapping("/ajaxProduct")
    public String ajaxProduct(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="") String keyword,
            Model model) {

        long total = productService.countProducts(keyword);
        model.addAttribute("products", productService.getAllProducts(page, size, keyword));
        model.addAttribute("page", page);
        model.addAttribute("pages", (int)Math.ceil((double)total/size));
        return "admin/product-table";
    }

    // 3. Hiển thị form tạo mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("mode", "create");
        // load danh mục và hãng
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("manufacturers", manufacturerService.getAll());
        return "admin/product-details"; // form chung
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") ProductDto productDto,
            BindingResult br,
            Model model,
            RedirectAttributes ra) {

        // Nếu có lỗi validate, trả lại form kèm error messages
        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            return "admin/product-details";
        }

        if (checkDuplicateProduct(productDto, model)) return "admin/product-details";

        Product product = modelMapper.map(productDto, Product.class);
        Category category = categoryService.findById(productDto.getCategory_id());
        Manufacturer manufacturer = manufacturerService.findById(productDto.getManufacturer_id());

        product.setCategory(category);
        product.setManufacturer(manufacturer);

        productService.saveOrUpdate(product);

        ra.addFlashAttribute("success", "Tạo sản phẩm thành công!");
        return "redirect:/admin/product";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id,
                             RedirectAttributes ra) {
        try {
            productService.deleteById(id);
            ra.addFlashAttribute("success", "Xoá sản phẩm thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("failed", "Xoá thất bại: " + e.getMessage());
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes ra) {
        ProductDto product = productService.findById(id);
        if (product == null) {
            ra.addFlashAttribute("failed", "Không tìm thấy sản phẩm");
            return "redirect:/admin/product";
        }

        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setCategory_id(product.getCategory_id());
        productDto.setManufacturer_id(product.getManufacturer_id());

        model.addAttribute("product", productDto);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("manufacturers", manufacturerService.getAll());
        model.addAttribute("mode", "edit");

        return "admin/product-details";
    }

    @PostMapping("/update")
    public String updateProduct(
            @ModelAttribute("product") @Valid ProductDto dto,
            BindingResult br,
            RedirectAttributes ra,
            Model model) {

        if (br.hasErrors()) {
            model.addAttribute("failed", "Cập nhật sản phẩm that bai!");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            model.addAttribute("mode", "edit");
            model.addAttribute("product", dto);
            return "admin/product-details";
        }

        if (checkDuplicateProduct(dto, model)) return "admin/product-details";

        Product product = productService.getProductById(dto.getProduct_id());

        if(product == null){
            model.addAttribute("failed", "Khong tim thay san pham");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            model.addAttribute("mode", "edit");
            model.addAttribute("product", dto);
            return "admin/product-details";
        }

        modelMapper.map(dto, product);

        Category category = categoryService.findById(dto.getCategory_id());
        Manufacturer manufacturer = manufacturerService.findById(dto.getManufacturer_id());
        product.setCategory(category);
        product.setManufacturer(manufacturer);

        productService.saveOrUpdate(product);
        ra.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        return "redirect:/admin/product";
    }

    private boolean checkDuplicateProduct(@ModelAttribute("product") @Valid ProductDto dto, Model model) {
        if(productService.getByName(dto.getName()) != null){
            model.addAttribute("failed", "Sản phẩm này đã tồn tại");
            model.addAttribute("mode", "create");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            return true;
        }
        return false;
    }
}
