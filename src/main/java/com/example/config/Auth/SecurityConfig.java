package com.example.config.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bỏ qua static resources
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/resources/**", "/css/**", "/js/**", "/images/**");
    }

    //====================================================
    // 1) Admin security – chạy trước (Order = 1)
    //====================================================
    @Configuration
    @Order(2)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Chỉ apply cho URL bắt đầu /admin/**
                    .requestMatcher(request -> request.getRequestURI().startsWith("/admin/"))
                    .authorizeRequests()
                    .anyRequest().hasRole("ADMIN")
                    .and()
                    .formLogin()
                    .loginPage("/admin/login")          // GET trang login admin
                    .loginProcessingUrl("/admin/login")   // POST form login
                    .usernameParameter("email")          // đọc trường email
                    .passwordParameter("password")       // đọc trường password
                    .successHandler(new LoginSuccessHandler())   // <–– dùng handler custom
//                    .failureUrl("/login/admin?error=true")
                    .permitAll()
                    .and()
//                    .logout()
//                    .logoutUrl("/admin/logout")
//                    .logoutSuccessUrl("/login/admin?logout")
//                    .permitAll()
//                    .and()
                    .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());  // Sử dụng PasswordEncoder
        }
    }

    //====================================================
    // 2) User security – chạy sau (Order = 2)
    //====================================================
    @Configuration
    @Order(1)
    public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    // public: trang chính (chứa modal), và static resources
                    .antMatchers("/", "/css/**", "/js/**",
                            "/user/login",
                            "/cart/**",
                            "/product/**",
                            "/chatbot/**").permitAll()
                    // URL khác (ngoại trừ /admin/** đã bị AdminConfig chặn) yêu cầu role USER
                    .anyRequest().hasRole("USER")
                    .and()
                    .formLogin()
                    .loginPage("/user/login")               // GET trang chính chứa modal
                    .loginProcessingUrl("/user/login") // POST form modal gửi đến
                    .usernameParameter("email")          // đọc trường email
                    .passwordParameter("password")       // đọc trường password
                    .defaultSuccessUrl("/", true)
//                    .failureUrl("/?error=true")
                    .permitAll()
                    .and()
//                    .logout()
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/")
//                    .permitAll()
//                    .and()
                    .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());  // Sử dụng PasswordEncoder
        }
    }
}
