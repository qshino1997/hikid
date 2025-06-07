package com.example.config.Auth;

import com.example.entity.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserService userService,
                                   RoleService roleService,
                                   PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        logger.info(">> OAuth2 user attributes: {}", oauth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // Lấy email + name
        String email = oauth2User.getAttribute("email");
        String name  = oauth2User.getAttribute("name");
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(name);
            user.setAuth_provider(registrationId);
            user.getRoles().add(roleService.findById(3));

            // Mật khẩu cứng, chỉ để có giá trị không null
            user.setPassword(passwordEncoder.encode("123456"));

            userService.saveOrUpdate(user);
            logger.info("→ Tạo mới user [{}] với authProvider=[{}]", email, registrationId);
        } else {
            boolean changed = false;
            assert name != null;
            if (!name.equals(user.getUsername())) {
                user.setUsername(name);
                changed = true;
            }
            if (user.getAuth_provider() == null) {
                user.setAuth_provider(registrationId);
                changed = true;
            }
            if (changed) {
                userService.saveOrUpdate(user);
                logger.info("→ Cập nhật user [{}]", email);
            }
        }


        Set<GrantedAuthority> authorities = new HashSet<>();
        boolean isAdmin   = user.getRoles().stream()
                .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getName()));
        boolean isManager = user.getRoles().stream()
                .anyMatch(r -> "MANAGER".equalsIgnoreCase(r.getName()));

        if (isAdmin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (isManager) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new CustomOAuth2User(oauth2User, authorities);

    }
}
