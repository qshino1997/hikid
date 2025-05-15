package com.example.controller.ChatBotController;

import com.example.service.ProductService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/dialogflow")
public class DialogflowWebhookController {

    @Autowired
    private ProductService productService;  // Service bạn tự viết để truy vấn DB

    @PostMapping(path = "/webhook",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String>  handleWebhook(@RequestBody String  payload) {
        // 1. Parse payload thành JsonObject
        JsonObject body = JsonParser.parseString(payload).getAsJsonObject();

        // 2. Lấy intent name
        String intentName = body
                .getAsJsonObject("queryResult")
                .getAsJsonObject("intent")
                .get("displayName")
                .getAsString();

        // 3. Lấy parameter "product" và "age_range"
        JsonObject params = body
                .getAsJsonObject("queryResult")
                .getAsJsonObject("parameters");

        String productName = params.has("product") && !params.get("product").isJsonNull()
                ? params.get("product").getAsString().trim()
                : "";

        String ageRange = params.has("age_range") && !params.get("age_range").isJsonNull()
                ? params.get("age_range").getAsString().trim()
                : "";


        // 4. Xác định replyText với fallback
        String replyText;
        if ("check_price".equals(intentName)) {
            if (productName.isEmpty()) {
                replyText = "Bạn muốn hỏi thương hiệu nào?";
            } else if (ageRange.isEmpty()) {
                replyText = String.format("Bạn hỏi sữa %s cho bé độ tuổi nào?", productName);
            } else {
                BigDecimal price = productService.findPriceByBrandAndAgeRange(productName, ageRange);
                if (price != null) {
                    replyText = String.format("Giá %s (%s) hiện tại là %,d₫.",
                            productName, ageRange, price.longValue());
                } else {
                    replyText = String.format("Không tìm thấy sữa %s cho độ tuổi %s.",
                            productName, ageRange);
                }
            }
        } else {
            // Fallback chung cho các intent khác
            replyText = "Mình chưa hiểu ý bạn, bạn có thể nói lại?";
        }

        // 5. Tạo response JSON
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("fulfillmentText", replyText);

        // 6. Trả về dưới dạng String
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse.toString());

    }
}