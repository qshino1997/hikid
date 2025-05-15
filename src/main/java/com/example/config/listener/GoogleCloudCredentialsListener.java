package com.example.config.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class GoogleCloudCredentialsListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String credentialsPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsPath == null || credentialsPath.isBlank()) {
            System.err.println("❌ GOOGLE_APPLICATION_CREDENTIALS không được thiết lập.");
        }

        File credentialsFile = new File(credentialsPath);
        if (!credentialsFile.exists()) {
            System.err.println("❌ File credentials không tồn tại tại: " + credentialsPath);
        }

        System.out.println("✅ GOOGLE_APPLICATION_CREDENTIALS đã được thiết lập đúng: " + credentialsPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[INFO] GoogleCloudCredentialsListener destroyed.");
    }
}
