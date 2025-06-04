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
        mode       = env.getProperty("PAYPAL_MODE");
        clientId       = env.getProperty("PAYPAL_CLIENTID");
        clientSecret          = env.getProperty("PAYPAL_CLIENTSECRET");
        webhookId          = env.getProperty("PAYPAL_WEBHOOKID");
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
