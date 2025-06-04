package com.example.config.Auth;

import com.example.dto.CartItemDto;
import com.example.dto.ProductDto;
import com.example.service.Cart.CartService;
import com.example.service.Cart.DatabaseCartService;
import com.example.service.Cart.SessionCartService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectProvider<CartService> dbCartProvider;
    private final UserService userService;

    @Autowired
    public UserLoginSuccessHandler(SessionCartService sessionCartService,
                                   @Qualifier("databaseCartService") ObjectProvider<CartService> dbCartProvider,
                                   UserService userService) {
        this.dbCartProvider = dbCartProvider;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        int userId = userService.findByEmail(authentication.getName()).getId();

        // Lấy instance prototype của bean databaseCartService
        CartService dbCartService = dbCartProvider.getObject();

        dbCartService.initForUser(userId);

        // Lấy giỏ hàng session từ session (không autowire trực tiếp)
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionCartService sessionCart = (SessionCartService)
                    session.getAttribute("scopedTarget.sessionCartService");

                for (CartItemDto it : sessionCart.getItems()) {
                    dbCartService.addItem(it.getProduct(), it.getQuantity());
                }
            }


        // Lưu cart mới (DatabaseCartService) vào session thay cho giỏ cũ
        request.getSession().setAttribute("cart", dbCartService);

        // Tiếp tục xử lý redirect hoặc logic khác nếu cần
        authentication.getAuthorities().forEach(grantedAuthority -> {
            try {
                if (grantedAuthority.getAuthority().equals("ROLE_ADMIN") || grantedAuthority.getAuthority().equals("ROLE_MANAGER")) {
                    response.sendRedirect("/admin/");
                } else {
                    response.sendRedirect("/");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
