package com.example.config.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //====================================================
    // 1) Admin security – chạy trước (Order = 1)
    //====================================================
    @Configuration
    @Order(1)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder _passwordEncoder;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Chỉ apply cho URL bắt đầu /admin/**
                    .requestMatcher(request -> request.getRequestURI().startsWith("/admin/"))
                    .authorizeRequests()
                    .antMatchers("/","/admin/login").permitAll()
                    .anyRequest().hasRole("ADMIN")
                    // Chỉ admin mới vào /admin/**
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/admin/login")          // GET trang login admin
                    .loginProcessingUrl("/admin/login")   // POST form login
                    .usernameParameter("email")          // đọc trường email
                    .passwordParameter("password")       // đọc trường password
                    .successHandler(new LoginSuccessHandler())   // <–– dùng handler custom
                    .failureUrl("/admin/login?error")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login?logout")
                    .permitAll()
                    .and()
                    .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(_passwordEncoder);  // Sử dụng PasswordEncoder
        }
    }

    //====================================================
    // 2) User security – chạy sau (Order = 2)
    //====================================================
    @Configuration
    @Order(2)
    public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder _passwordEncoder;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .requestMatcher(request -> request.getRequestURI().startsWith("/"))
                    .authorizeRequests()
                    // 1) Cho phép mọi truy cập GET/POST đến webhook
                    .antMatchers(HttpMethod.POST, "/payment/webhook","/dialogflow/webhook").permitAll()
                    // public: trang chính (chứa modal), và static resources
                    .antMatchers("/", "/css/**", "/js/**",
                            "/user/login",
                            "/cart/**",
                            "/product/**",
                            "/chatbot/**",
                            "/order/**").permitAll()
                    // URL khác (ngoại trừ /admin/** đã bị AdminConfig chặn) yêu cầu role USER
                    .anyRequest().hasRole("USER")
                    .and()
                    .formLogin()
                    .loginPage("/user/login")               // GET trang chính chứa modal
                    .loginProcessingUrl("/user/login") // POST form modal gửi đến
                    .usernameParameter("email")          // đọc trường email
                    .passwordParameter("password")       // đọc trường password
                    .successHandler(new LoginSuccessHandler())   // <–– dùng handler custom
                    .failureUrl("/?error")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
                    .permitAll()
                    .and()
                    .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(_passwordEncoder);  // Sử dụng PasswordEncoder
        }
    }
}
