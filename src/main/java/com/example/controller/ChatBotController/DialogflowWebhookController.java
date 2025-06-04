package com.example.controller.ChatBotController;

import com.example.dto.ProductDto;
import com.example.service.WebhookDialogFlowService.ProductWebhookService;
import com.example.service.WebhookDialogFlowService.WebhookProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/dialogflow")
public class DialogflowWebhookController {

    @Autowired
    private WebhookProcessor webhookProcessor;

    @PostMapping(value = "/webhook",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> handleWebhook(HttpServletRequest request, HttpSession httpSession) throws IOException {
        // Đọc payload JSON từ request
        byte[] bytes = StreamUtils.copyToByteArray(request.getInputStream());
        String payload = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(payload);
        JsonObject body = JsonParser.parseString(payload).getAsJsonObject();

        JsonObject jsonResponse = webhookProcessor.process(body);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);

    }
}