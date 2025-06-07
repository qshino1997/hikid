package com.example.util;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    private UserService userService;

    /**
     * Trả về CustomUserDetails nếu đã authenticate, ngược lại trả về null
     */
    private Object getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return auth.getPrincipal();
    }

    /**
     * Trả về userId (hoặc null nếu chưa login)
     */
    public Integer getCurrentId() {
        String email = getCurrentEmail();
        if (email == null) {
            return null;
        }
        // Giả sử userService.findByEmail(email) trả về Optional<User>
        return userService.findByEmail(email).getId();
    }

    /**
     * Trả về username (email) nếu đã login, ngược lại trả về null
     */
    public String getCurrentEmail() {
        Object principal = getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        if (principal instanceof OAuth2User) {
            return ((OAuth2User) principal).getAttribute("email");
        }
        return null;
    }

}
