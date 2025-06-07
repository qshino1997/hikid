package com.example.config.Auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User delegate;
    private final Set<GrantedAuthority> authorities;

    public CustomOAuth2User(OAuth2User delegate, Set<GrantedAuthority> authorities) {
        this.delegate = delegate;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        // Bạn muốn principal.name là email:
        return getAttributes().get("email").toString();
    }

    // Thêm helper getUsername() nếu bạn dùng principal.username
    public String getUsername() {
        return getName();
    }
}

