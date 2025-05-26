package com.example.controller.Admin;

import com.example.dto.UserProfileDto;
import com.example.entity.User;
import com.example.entity.UserProfile;
import com.example.service.UserProfileService;
import com.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = {"/user","/"})
    public String listUsers(
                            Model model) {
        List<User> users = userService.getAllEmployees();

        List<UserProfileDto> dtoUserList = new ArrayList<>();
        for (User user : users) {
            UserProfileDto dto = modelMapper.map(user, UserProfileDto.class);
            dto.setUser_id(user.getId());
            if (user.getProfile() != null) {
                dto.setDate_of_birth(user.getProfile().getDate_of_birth());
                dto.setPhone(user.getProfile().getPhone());
                dto.setAddress(user.getProfile().getAddress());
            }

            dtoUserList.add(dto);
        }
        model.addAttribute("page","user");
        model.addAttribute("users", dtoUserList);
        return "admin.user";
    }

    @GetMapping("/customer")
    public String listCustomers(Model model) {
        List<User> users2 = userService.getAllCustomers();
        List<UserProfileDto> dtoUserList2 = new ArrayList<>();
        for (User user : users2) {
            UserProfileDto dto = modelMapper.map(user, UserProfileDto.class);
            dto.setUser_id(user.getId());

            if (user.getProfile() != null) {
                dto.setDate_of_birth(user.getProfile().getDate_of_birth());
                dto.setPhone(user.getProfile().getPhone());
                dto.setAddress(user.getProfile().getAddress());
            }

            dtoUserList2.add(dto);
        }
        model.addAttribute("page","customer");
        model.addAttribute("users", dtoUserList2);
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
                                Model model,
                                Principal principal) {

        if (br.hasErrors()) {
            model.addAttribute("user", dto);
            model.addAttribute("mode", "create");
            return "admin/profile";
        }

        if (dto.getUser_id() == null) {
            if(userService.findByEmail((dto.getEmail())) != null){
                model.addAttribute("failed", "Email da ton tai, vui long nhap email khac");
            } else {
                // ==== TẠO MỚI ====
                User newUser = new User();
                newUser.setUsername(dto.getUsername());
                newUser.setEmail(dto.getEmail());
                // bạn có thể set password mặc định hoặc để admin nhập
                newUser.setPassword(passwordEncoder.encode("123456"));
                UserProfile profile = new UserProfile();
                profile.setUser(newUser);           // set liên kết 2 chiều
                profile.setDate_of_birth(dto.getDate_of_birth());
                profile.setPhone(dto.getPhone());
                profile.setAddress(dto.getAddress());
                newUser.setProfile(profile);
                // Nếu cần set role:
                // newUser.setRoles(Collections.singleton(roleService.findByName("ROLE_EMPLOYEE")));
                userService.saveOrUpdate(newUser);

                model.addAttribute("success", "Tạo nhân viên mới thành công!");
                model.addAttribute("mode", "create");
                // Nội dung DTO sau tạo mới: có thể reset form
                model.addAttribute("user", new UserProfileDto());
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
