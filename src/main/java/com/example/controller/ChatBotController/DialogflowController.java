package com.example.controller.ChatBotController;

import com.example.dto.chatbotDto.ChatResponse;
import com.example.dto.chatbotDto.QuestionRequest;
import com.example.service.ChatBotService.DialogflowService;
import com.example.service.ProductService;
import com.example.service.WebhookDialogFlowService.ProductWebhookService;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryResult;
import org.apache.commons.beanutils.converters.NumberConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/chatbot")
public class DialogflowController {
    @Autowired
    private DialogflowService dialogflowService;

    @Autowired
    private ProductWebhookService productWebhookService;

    // API để gửi câu hỏi tới Dialogflow và nhận phản hồi
    @PostMapping(value = "/ask", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> askQuestion(@RequestBody QuestionRequest questionRequest) {
        String userQuery = questionRequest.getUserQuery();
        if (userQuery == null || userQuery.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ChatResponse("Vui lòng nhập câu hỏi"));
        }
        try {
            // gọi về getResponseFromDialogflow() để lấy fulfillmentText
            String reply = dialogflowService.getResponseFromDialogflow(userQuery);

            // --- Bước 1: Gọi DetectIntent trả về đầy đủ Response (để lấy intentName + outputContexts) ---
            DetectIntentResponse detectResponse =
                    dialogflowService.detectIntentReturnFullResponse(userQuery);
            QueryResult queryResult = detectResponse.getQueryResult();

            // --- Bước 2: Lấy intentName ---
            String intentName = "";
            if (queryResult.hasIntent()) {
                intentName = queryResult.getIntent().getDisplayName();
            }

            Integer productId = 0;
            // --- Bước 3: Nếu intent là AddToCart
            if ("AddToCart".equals(intentName)) {
                String productIdStr = reply.replace("Đã thêm sản phẩm vào giỏ hàng thành công!", "").trim();
                try{
                    productId = Integer.parseInt(productIdStr);
                } catch (Exception ignored){
                }
                if(productId != 0){
                    productWebhookService.addToCart(productId);
                    reply = "Đã thêm sản phẩm vào giỏ hàng thành công!";
                    return ResponseEntity.ok(new ChatResponse(reply, productWebhookService.getQualityCart()));
                }
            }

            return ResponseEntity.ok(new ChatResponse(reply));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ChatResponse("Đã có lỗi xảy ra, vui lòng thử lại sau."));
        }
    }
}
