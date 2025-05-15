package com.example.config.Auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 1. Lấy username vừa đăng nhập
        String username = authentication.getName();
        // 2. Ghi log, cập nhật last login time, v.v.
        System.out.println("User " + username + " has logged in successfully");

        // 3. Chuyển hướng (redirect) về trang phù hợp theo role
        authentication.getAuthorities().forEach(grantedAuthority -> {
            try {
                if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                    response.sendRedirect("/admin/dashboard");
                } else {
                    response.sendRedirect("/home");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
