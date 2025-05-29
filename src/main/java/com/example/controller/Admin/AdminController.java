package com.example.controller.Admin;

import com.example.dto.UserProfileDto;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.UserProfile;
import com.example.service.RoleService;
import com.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = {"/user","/"})
    public String listUsers() {
        return "admin.user";
    }

    @GetMapping("/ajaxUser")
    public String loadUsersAjax(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="5") int size,
            @RequestParam int role,
            @RequestParam String keyword,
            Model model) {

        List<UserProfileDto> dtos = userService.getAllUsers(page, size, role, keyword).stream()
                .map(u -> {
                    // Map entity User sang DTO
                    UserProfileDto dto = modelMapper.map(u, UserProfileDto.class);
                    dto.setUser_id(u.getId());
                    if (u.getProfile() != null) {
                        dto.setDate_of_birth(u.getProfile().getDate_of_birth());
                        dto.setPhone(u.getProfile().getPhone());
                        dto.setAddress(u.getProfile().getAddress());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        long total = userService.countEmployees(role, keyword);
        model.addAttribute("users", dtos);
        model.addAttribute("page", page);
        model.addAttribute("pages", (int)Math.ceil((double)total/size));
        model.addAttribute("keywordDefault", keyword);

        if(role == 3){
            return "admin/customer-table";
        }
        return "admin/user-table";
    }


    @GetMapping("/customer")
    public String listCustomers() {
        return "admin.customer";
    }

    // === Profile của chính mình ===
    @GetMapping("/{id}/profile")
    public String viewProfile(@PathVariable int id, Model model, Principal principal) {
        User user = new User();
        if(id != -1){
            user = userService.findById(id);
            model.addAttribute("mode", "edit");
        } else {
            model.addAttribute("mode", "update");
            user = userService.findByEmail(principal.getName());
        }

        UserProfileDto dto = modelMapper.map(user, UserProfileDto.class);
        dto.setUser_id(user.getId());

        if (user.getProfile() != null) {
            UserProfileDto nested = modelMapper.map(user.getProfile(), UserProfileDto.class);
            dto.setDate_of_birth(nested.getDate_of_birth());
            dto.setPhone(nested.getPhone());
            dto.setAddress(nested.getAddress());
        }

        model.addAttribute("user", dto);

        return "admin/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("user") UserProfileDto dto,
                                BindingResult br,
                                RedirectAttributes ra,
                                Model model) {

        if (br.hasErrors()) {
            model.addAttribute("user", dto);
            model.addAttribute("mode", "create");
            return "admin/profile";
        }

        if (dto.getUser_id() == null) {
            if(userService.findByEmail((dto.getEmail())) != null){
                model.addAttribute("user", dto);
                model.addAttribute("failed", "Email da ton tai, vui long nhap email khac");
                model.addAttribute("mode", "create");
                return "admin/profile";
            } else {
                // ==== TẠO MỚI ====
                User newUser = new User();
                newUser.setUsername(dto.getUsername());
                newUser.setEmail(dto.getEmail());
                newUser.setPassword(passwordEncoder.encode("123456"));

                // Gán role mặc định EMPLOYEE (id = 2)
                newUser.getRoles().add(roleService.findById(2));

                UserProfile profile = new UserProfile();
                profile.setUser(newUser);           // set liên kết 2 chiều
                profile.setDate_of_birth(dto.getDate_of_birth());
                profile.setPhone(dto.getPhone());
                profile.setAddress(dto.getAddress());
                newUser.setProfile(profile);

                userService.saveOrUpdate(newUser);

                ra.addFlashAttribute("success", "Tạo nhân viên mới thành công!");
                return "redirect:/admin/";
            }
        } else {
            User user = userService.findById(dto.getUser_id());
            if (user != null) {
                user.setUsername(dto.getUsername());
                if (user.getProfile() == null) {
                    user.setProfile(new UserProfile());
                }
                user.getProfile().setDate_of_birth(dto.getDate_of_birth());
                user.getProfile().setPhone(dto.getPhone());
                user.getProfile().setAddress(dto.getAddress());
                userService.saveOrUpdate(user);

                model.addAttribute("success", "Cập nhật thành công");
            } else {
                model.addAttribute("failed", "Cập nhật thất bại");
            }

            model.addAttribute("mode", "edit");
            model.addAttribute("user", dto);
        }
        return "admin/profile";
    }

    @PostMapping("/password")
    public String changePassword(
            @RequestParam("user_id") int userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        User user = userService.findById(userId);

        // Convert sang DTO để gán vào form
        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDate_of_birth(user.getProfile().getDate_of_birth());
        dto.setPhone(user.getProfile().getPhone());
        dto.setAddress(user.getProfile().getAddress());

        // Gán vào model cho form:form
        model.addAttribute("user", dto);

        // Kiểm tra mật khẩu cũ đúng không
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("pwdError", "Mật khẩu cũ không đúng");
            return "admin/profile";
        }
        // Kiểm tra new vs confirm
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("pwdError", "Mật khẩu mới và xác nhận không khớp");
            return "admin/profile";
        }

        // Lưu mật khẩu mới (hash bằng BCrypt)
        userService.updatePassword(user.getEmail(), passwordEncoder.encode(newPassword));
        model.addAttribute("pwdSuccess", "Đổi mật khẩu thành công");
        return "admin/profile";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id,
                             RedirectAttributes ra) {
        try {
            userService.deleteById(id);
            ra.addFlashAttribute("success", "Xoá người dùng thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("failed", "Xoá thất bại: " + e.getMessage());
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        UserProfileDto dto = new UserProfileDto();
        // nếu cần default ngày sinh, role, v.v.
        model.addAttribute("user", dto);
        model.addAttribute("mode", "create");
        return "admin/profile";
    }

}
