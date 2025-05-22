package com.example.config.Payment;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
public class PayPalConfig {
    @Autowired
    private Environment env;

    private String mode;
    private String clientId;
    private String clientSecret;
    private String webhookId;

    @PostConstruct
    public void init() {
        mode       = env.getProperty("paypal.mode");
        clientId       = env.getProperty("paypal.clientId");
        clientSecret          = env.getProperty("paypal.clientSecret");
        webhookId          = env.getProperty("paypal.webhookId");
    }

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("mode", mode);
        return config;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }
}
