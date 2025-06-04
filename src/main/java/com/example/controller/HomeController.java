package com.example.controller;

import com.example.dto.Form.RegisterForm;
import com.example.dto.ProductDto;
import com.example.dto.UserProfileDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.RoleService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    @GetMapping("/")
    public String home(Model model){
        List<Category> categories = categoryService.getRootCategoriesWithSubs();
        List<ProductDto> milkList = productService.getMaxProducesBySix(3);
        List<ProductDto> vitaminList = productService.getMaxProducesBySix(4);
        List<ProductDto> toyList = productService.getMaxProducesBySix(1);
        List<ProductDto> clothesList = productService.getMaxProducesBySix(2);

        model.addAttribute("milkList", milkList);
        model.addAttribute("vitaminList", vitaminList);
        model.addAttribute("toyList", toyList);
        model.addAttribute("clothesList", clothesList);

        model.addAttribute("categories", categories);

        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterForm());
        }

        return "home";
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class,
                new org.springframework.beans.propertyeditors.CustomNumberEditor(Integer.class, true));
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                           BindingResult br,
                           RedirectAttributes ra){

        if(br.hasErrors()){
            ra.addFlashAttribute("openRegisterModal", true);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.registerForm", br);
            ra.addFlashAttribute("registerForm", registerForm);
            return "redirect:/";
        }

        // Kiểm tra new vs confirm
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "password.mismatch", "Mật khẩu và xác nhận không khớp");
            ra.addFlashAttribute("openRegisterModal", true);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.registerForm", br);
            ra.addFlashAttribute("registerForm", registerForm);
            return "redirect:/";
        }

        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));

        Role roleEmployee = roleService.findById(3);
        // Gán role mặc định EMPLOYEE (id = 2)
        user.getRoles().add(roleEmployee);

        userService.saveOrUpdate(user);
        ra.addFlashAttribute("success", "Tạo tài khoản thành công!");

        return "redirect:/";
    }

}
