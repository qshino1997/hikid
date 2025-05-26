package com.example.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.io.File;


public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 0) Thiết lập GOOGLE_APPLICATION_CREDENTIALS trước khi khởi Spring
//        String real = servletContext.getRealPath("/WEB-INF/credentials/dialogflow-key.json");
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "/etc/secrets/dialogflow-key.json");

//        if (real != null && new File(real).exists()) {
//            System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", real);
//            servletContext.log("Set GOOGLE_APPLICATION_CREDENTIALS = " + real);
//        } else {
//            servletContext.log("WARNING: credentials file not found at " + real);
//        }

        // 1) Gọi vào Spring để khởi ContextLoaderListener và DispatcherServlet
        super.onStartup(servletContext);
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[]{ encodingFilter };
    }

    // Root context (AppConfig)
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ AppConfig.class };
    }

    // Web context (WebConfig)
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ WebConfig.class };
    }

    // Mapping cho DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/" };
    }
}