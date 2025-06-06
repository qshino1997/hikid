package com.example.controller.Admin;

import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Manufacturer;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.service.CategoryService;
import com.example.service.Github.GitHubService;
import com.example.service.ManufacturerService;
import com.example.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;

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

    @Autowired
    private GitHubService gitHubService;

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
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model,
            RedirectAttributes ra) {

        // Nếu có lỗi validate, trả lại form kèm error messages
        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            return "admin/product-details";
        }

        Product product = modelMapper.map(productDto, Product.class);
        try{
            Category category = categoryService.findById(productDto.getCategory_id());
            Manufacturer manufacturer = manufacturerService.findById(productDto.getManufacturer_id());

            product.setCategory(category);
            product.setManufacturer(manufacturer);

            ProductImage productImage = new ProductImage();

            // Xử lý upload ảnh mới nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String pathInRepo = "product-images/" + fileName;
                String commitMessage = "Update product image: " + fileName;

                byte[] content = imageFile.getBytes();
                String imageUrl = gitHubService.uploadFile(pathInRepo, content, commitMessage);

                productImage.setUrl(imageUrl);
                productImage.setProduct(product);
                productImage.setIs_primary(true);

                // Cập nhật URL mới vào DTO (để lưu vào entity)
            }

            product.setImage(productImage);

            productService.saveOrUpdate(product);
        } catch (Exception e){
            ra.addFlashAttribute("failed", "Lỗi khi them moi sản phẩm");
            System.out.println(e.getMessage());
            return "redirect:/admin/product";
        }


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
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes ra,
            Model model) {
        // Validate form
        if (br.hasErrors()) {
            model.addAttribute("failed", "Cập nhật sản phẩm that bai!");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            model.addAttribute("mode", "edit");
            model.addAttribute("product", dto);
            return "admin/product-details";
        }

        // Lấy product cũ từ DB
        Product product = productService.getProductById(dto.getProduct_id());
        if(product == null){
            model.addAttribute("failed", "Khong tim thay san pham");
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("manufacturers", manufacturerService.getAll());
            model.addAttribute("mode", "edit");
            model.addAttribute("product", dto);
            return "admin/product-details";
        }

        try{
            // Xử lý upload ảnh mới nếu có
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String pathInRepo = "product-images/" + fileName;
                String commitMessage = "Update product image: " + fileName;

                byte[] content = imageFile.getBytes();
                String imageUrl = gitHubService.uploadFile(pathInRepo, content, commitMessage);

                // Cập nhật URL mới vào DTO (để lưu vào entity)
                dto.setUrl(imageUrl);
            } else {
                // Nếu không upload mới, giữ nguyên URL cũ
                dto.setUrl(product.getImage().getUrl());
            }

            // Map các trường còn lại từ DTO sang entity
            modelMapper.map(dto, product);
            if(product.getImage() != null){
                product.getImage().setUrl(dto.getUrl().isEmpty() ? "" : dto.getUrl());
            } else {
                if(!dto.getUrl().isEmpty()){
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setIs_primary(true);
                    productImage.setUrl(dto.getUrl());
                    productImage.setProduct(product);
                    product.setImage(productImage);
                }
            }
            // Cập nhật quan hệ Category và Manufacturer
            Category category = categoryService.findById(dto.getCategory_id());
            Manufacturer manufacturer = manufacturerService.findById(dto.getManufacturer_id());
            product.setCategory(category);
            product.setManufacturer(manufacturer);
            // Lưu vào DB
            productService.saveOrUpdate(product);
            ra.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        }catch (Exception ex){
            ra.addFlashAttribute("failed", "Lỗi khi cập nhật sản phẩm");
            System.out.println(ex.getMessage());
            return "redirect:/admin/product";
        }

        return "redirect:/admin/product";
    }
}
