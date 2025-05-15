package com.example.controller.ChatBotController;

import com.example.dto.chatbotDto.ChatResponse;
import com.example.dto.chatbotDto.QuestionRequest;
import com.example.service.ChatBotService.DialogflowService;
import com.google.cloud.dialogflow.v2.Intent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/chatbot")
public class DialogflowController {
    @Autowired
    private DialogflowService dialogflowService;

    // API để gửi câu hỏi tới Dialogflow và nhận phản hồi
    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@RequestBody QuestionRequest questionRequest) {
        String userQuery = questionRequest.getUserQuery();
        if (userQuery == null || userQuery.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ChatResponse("Vui lòng nhập câu hỏi"));
        }

        try {
            String reply = dialogflowService.getResponseFromDialogflow(userQuery);
            return ResponseEntity.ok(new ChatResponse(reply));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ChatResponse("Đã có lỗi xảy ra, vui lòng thử lại sau."));
        }
    }
}
