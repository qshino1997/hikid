package com.example.controller.Payment;

import com.example.config.Payment.PayPalConfig;
import com.example.service.OrderService;
import com.example.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequestMapping("/payment")
public class OrderWebhookController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OAuthTokenCredential oAuthTokenCredential;

    @Autowired
    private PayPalConfig payPalConfig;

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleWebhook(
            HttpServletRequest request) {

        try {

            byte[] bytes = StreamUtils.copyToByteArray(request.getInputStream());
            String rawPayload = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(rawPayload);
            // Lấy các header của webhook gửi đến
            String transmissionId   = request.getHeader("PayPal-Transmission-Id");
            String transmissionTime = request.getHeader("PayPal-Transmission-Time");
            String certUrl          = request.getHeader("PayPal-Cert-Url");
            String authAlgo         = request.getHeader("PayPal-Auth-Algo");
            String transmissionSig  = request.getHeader("PayPal-Transmission-Sig");

            // Lấy access token từ apiContext
            String accessToken = oAuthTokenCredential.getAccessToken();

            // Build raw JSON payload cho verify-webhook-signature bằng String concatenation
            String jsonPayload = "{" +
                    "\"transmission_id\":\"" + transmissionId + "\"," +
                    "\"transmission_time\":\"" + transmissionTime + "\"," +
                    "\"cert_url\":\"" + certUrl + "\"," +
                    "\"auth_algo\":\"" + authAlgo + "\"," +
                    "\"transmission_sig\":\"" + transmissionSig + "\"," +
                    "\"webhook_id\":\"" + payPalConfig.getWebhookId() + "\"," +
                    "\"webhook_event\":" + rawPayload +
                    "}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
            }
            headers.setBearerAuth(accessToken);


            System.out.println("Access Token: " + accessToken);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

            // Gửi verify đến PayPal
            String url = "https://api.sandbox.paypal.com/v1/notifications/verify-webhook-signature";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("Verify webhook response: " + response.getBody());

            JsonObject respJson = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();
            String verificationStatus = respJson.get("verification_status").getAsString();

            if ("SUCCESS".equals(verificationStatus)) {
                // Parse lại event từ rawPayload (dòng bạn đã đọc nguyên văn body)
                JsonObject webhookEvent = JsonParser
                        .parseString(rawPayload)
                        .getAsJsonObject();

                String eventType = webhookEvent.get("event_type").getAsString();
                if ("PAYMENTS.PAYMENT.CREATED".equals(eventType)) {
                    JsonObject resource = webhookEvent.getAsJsonObject("resource");
                    // Kiểm tra nếu có "id"
                    if (resource.has("id")) {
                        String captureId = resource.get("id").getAsString();

                        // Gọi xử lý thanh toán với captureId
                        orderService.handleWebhookEvent(captureId);

                        return ResponseEntity.ok("{\"status\":\"success\"}");
                    } else {
                        System.out.println("resource.id not found in webhook event");
                    }
                }

                return ResponseEntity.ok("{\"status\":\"success\"}");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"status\":\"verify_failed\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
