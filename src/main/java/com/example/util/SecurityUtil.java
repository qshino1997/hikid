package com.example.util;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    private UserService userService;

    /**
     * Trả về CustomUserDetails nếu đã authenticate, ngược lại trả về null
     */
    public UserDetails getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && auth.getPrincipal() instanceof UserDetails
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return (UserDetails) auth.getPrincipal();
        }
        return null;
    }

    /**
     * Trả về userId (hoặc null nếu chưa login)
     */
    public Integer getCurrentId() {
        String email = getCurrentEmail();
        if (email == null) {
            return null;
        }
        return userService.findByEmail(email).getId();
    }

    /**
     * Trả về username (email) nếu đã login, ngược lại trả về null
     */
    public String getCurrentEmail() {
        UserDetails ud = getCurrentUserDetails();
        return (ud != null ? ud.getUsername() : null);
    }

}
