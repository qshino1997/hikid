package com.example.config;

import com.example.config.listener.GoogleCloudCredentialsListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        String credentialsJson = System.getenv("DIALOGFLOW_CREDENTIALS_JSON");
        if (credentialsJson != null) {
            // Lưu ra file tạm
            Path tempFile = null;
            try {
                tempFile = Files.createTempFile("dialogflow-key", ".json");
                Files.write(tempFile, credentialsJson.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", tempFile.toString());
        } else {
            throw new RuntimeException("Missing environment variable DIALOGFLOW_CREDENTIALS_JSON");
        }

        // 1) Đăng ký Google Cloud Listener trước để set GOOGLE_APPLICATION_CREDENTIALS
        servletContext.addListener((ServletContextListener) new GoogleCloudCredentialsListener());

        // 2) Root ApplicationContext chứa AppConfig
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // 3) Web ApplicationContext chứa WebConfig
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);

        // 4) Đăng ký DispatcherServlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(webContext)
        );
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }
}