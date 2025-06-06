package com.example.config.Auth;

import com.example.config.Auth.Google.CustomOAuth2UserService;
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

// OAuth2 imports
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ==== 0) Bỏ qua static resources (CSS/JS/Images) toàn cục ====
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/resources/**", "/css/**", "/js/**", "/images/**");
    }

    // ==== 0.b) BCryptPasswordEncoder dùng chung ====
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ============================
    // 1) ADMIN SECURITY (Order = 1)
    // ============================
    @Configuration
    @Order(1)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Chỉ apply cho mọi URL bắt đầu /admin/
                    .requestMatcher(request -> request.getRequestURI().startsWith("/admin/"))
                    .authorizeRequests()
                    .antMatchers("/admin/login").permitAll()
                    .anyRequest().hasAnyRole("ADMIN", "MANAGER")
                    .and()
                    // Form‐login admin
                    .formLogin()
                    .loginPage("/admin/login")          // GET hiển thị form
                    .loginProcessingUrl("/admin/login") // POST xử lý login
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureUrl("/admin/login?error")
                    .permitAll()
                    .and()
                    // Logout admin
                    .logout()
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login?logout")
                    .permitAll()
                    .and()
                    // 403 Admin
                    .exceptionHandling()
                    .accessDeniedPage("/403")
                    .and()
                    .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
        }
    }

    // ============================
    // 2) USER SECURITY (Order = 2)
    // ============================
    @Configuration
    @Order(2)
    public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserLoginSuccessHandler userLoginSuccessHandler;

        // **Bắt buộc** inject hai bean này từ OAuth2ClientConfig
        @Autowired
        private ClientRegistrationRepository clientRegistrationRepository;

        @Autowired
        private OAuth2AuthorizedClientService authorizedClientService;

        @Autowired
        private CustomOAuth2UserService customOAuth2UserService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()

                    // 1) Phân quyền request
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/payment/webhook", "/dialogflow/webhook")
                    .permitAll()
                    .antMatchers(
                            "/", "/css/**", "/js/**",            // Trang chính/public
                            "/user/login", "/register",
                            "/cart/**", "/product/**", "/chatbot/**",
                            "/order/checkout", "/order/success", "/order/cancel"
                    ).permitAll()
                    .antMatchers("/oauth2/**", "/login/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .hasRole("USER")
                    .and()

                    // 2) OAuth2 Login (Google) – PHẢI gọi hai bean bên dưới
                    .oauth2Login()
                    .clientRegistrationRepository(clientRegistrationRepository)
                    .authorizedClientService(authorizedClientService)
                    .loginPage("/")   // nếu chưa login → redirect về "/"
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/?showLogin=true&error")
                    .and()

                    // 3) Form Login cho USER (modal trên "/")
                    .formLogin()
                    .loginPage("/")                  // GET "/" show modal login
                    .loginProcessingUrl("/user/login") // POST form đến "/user/login"
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(userLoginSuccessHandler)
                    .failureUrl("/?showLogin=true&error")
                    .permitAll()
                    .and()

                    // 4) Logout cho USER
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
                    .permitAll()
                    .and()

                    // 5) 403 USER
                    .exceptionHandling()
                    .accessDeniedPage("/403");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
        }
    }
}
