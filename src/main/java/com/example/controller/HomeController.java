package com.example.controller;

import com.example.dto.CartItemDto;
import com.example.dto.Form.RegisterForm;
import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.Cart.CartService;
import com.example.service.Cart.SessionCartService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.RoleService;
import com.example.service.UserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("databaseCartService")
    private ObjectProvider<CartService> dbCartProvider;

    @Autowired
    @Qualifier("sessionCartService")
    private SessionCartService sessionCartService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oauth2User,
                       Authentication authentication,
                       HttpServletRequest request){

        // 1) Kiểm tra xem authentication có thực sự là OAuth2AuthenticationToken không
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            oauth2User = (OAuth2User) oauthToken.getPrincipal();

            // Lấy thông tin email, name, registrationId
            String email = oauth2User.getAttribute("email");
            String name  = oauth2User.getAttribute("name");
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();

            // Tìm hoặc tạo mới User theo email
            User user = userService.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setUsername(name);
                user.setAuth_provider(registrationId);
                Role defaultRole = roleService.findById(3);
                if (defaultRole != null) {
                    user.getRoles().add(defaultRole);
                }
                user.setPassword(passwordEncoder.encode("123456"));
                userService.saveOrUpdate(user);
            }
            else {
                // Nếu user đã có sẵn, kiểm tra xem có phải ADMIN không
                boolean isAdmin = user.getRoles().stream()
                        .anyMatch(r -> r.getName().equalsIgnoreCase("admin"));
                if (!isAdmin) {
                    boolean needUpdate = false;
                    if (!name.equals(user.getUsername())) {
                        user.setUsername(name);
                        needUpdate = true;
                    }
                    if (user.getAuth_provider() == null) {
                        user.setAuth_provider(registrationId);
                        needUpdate = true;
                    }
                    if (needUpdate) {
                        userService.saveOrUpdate(user);
                    }
                }

                // Lấy instance prototype của bean databaseCartService
                CartService dbCartService = dbCartProvider.getObject();

                dbCartService.initForUser(user.getId());

                // Lấy giỏ hàng session từ session (không autowire trực tiếp)
                for (CartItemDto it : sessionCartService.getItems()) {
                    dbCartService.addItem(it.getProduct(), it.getQuantity());
                }

                model.addAttribute("cart", dbCartService);
                model.addAttribute("cartCount", dbCartService.getTotalQuantity());

                sessionCartService.clear();
            }

            // Build lại AuthenticationToken mới để giữ session đã xác thực
            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken newAuth =
                    getUsernamePasswordAuthenticationToken(user, authorities);
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

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

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(User user, List<SimpleGrantedAuthority> authorities) {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),                // username
                user.getPassword(),             // password (đã mã hóa, nhưng không dùng để so sánh lúc này)
                authorities                     // danh sách GrantedAuthority (ví dụ ROLE_USER)
        );

        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,                // principal là UserDetails
                        null,                       // credentials = null vì đã xác thực qua OAuth2
                        userDetails.getAuthorities()// authorities
                );
        return newAuth;
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
