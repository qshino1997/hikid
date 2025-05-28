package com.example.controller;

import com.example.dto.UserProfileDto;
import com.example.entity.User;
import com.example.entity.UserProfile;
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

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model,
                          Principal principal,
                          RedirectAttributes ra){
        User user = userService.findByEmail(principal.getName());

        if(user == null){
            ra.addFlashAttribute("failed", "Không tìm thấy thông tin người dùng");
            return "redirect:/";
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

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("user") UserProfileDto dto,
                                BindingResult br,
                                Model model) {
        if (br.hasErrors()) {
            model.addAttribute("user", dto);
            return "profile";
        }

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

        model.addAttribute("user", dto);

        return "profile";
    }

    @PostMapping("/password")
    public String changePassword(
            @RequestParam("user_id") int userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        User user = userService.findById(userId);

        UserProfileDto dto = modelMapper.map(user, UserProfileDto.class);
        dto.setUser_id(user.getId());

        if (user.getProfile() != null) {
            UserProfileDto nested = modelMapper.map(user.getProfile(), UserProfileDto.class);
            dto.setDate_of_birth(nested.getDate_of_birth());
            dto.setPhone(nested.getPhone());
            dto.setAddress(nested.getAddress());
        }

        // Gán vào model cho form:form
        model.addAttribute("user", dto);

        // Kiểm tra mật khẩu cũ đúng không
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("pwdError", "Mật khẩu cũ không đúng");
            return "profile";
        }
        // Kiểm tra new vs confirm
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("pwdError", "Mật khẩu mới và xác nhận không khớp");
            return "profile";
        }

        // Lưu mật khẩu mới (hash bằng BCrypt)
        userService.updatePassword(user.getEmail(), passwordEncoder.encode(newPassword));
        model.addAttribute("pwdSuccess", "Đổi mật khẩu thành công");
        return "profile";
    }
}
