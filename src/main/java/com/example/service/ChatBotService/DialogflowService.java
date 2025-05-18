package com.example.service.ChatBotService;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.UUID;

@Service
public class DialogflowService {
    private SessionsClient sessionsClient;
    private final String projectId = "trolybanhang-fskm";

    @PostConstruct
    public void init() throws Exception {
        String credentialsPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");

        File credentialsFile = new File(credentialsPath);
        if (!credentialsFile.exists()) {
            throw new FileNotFoundException("❌ File credentials không tồn tại tại: " + credentialsPath);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFile));

        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        // Khởi tạo SessionsClient với Default Credentials (từ GOOGLE_APPLICATION_CREDENTIALS)
        sessionsClient = SessionsClient.create(sessionsSettings);
    }

    /**
     * Gửi userQuery lên Dialogflow và trả về fulfillmentText
     */
    public String getResponseFromDialogflow(String userQuery) throws Exception {
        if (userQuery == null || userQuery.isBlank()) {
            return "Mình chưa nhận được câu hỏi, bạn vui lòng nhập lại.";
        }

        // Tạo sessionId độc nhất để Dialogflow gom context trong phiên đó
        String sessionId = UUID.randomUUID().toString();
        SessionName session = SessionName.of(projectId, sessionId);

        // Xây dựng input
        TextInput textInput = TextInput.newBuilder()
                .setText(userQuery)
                .setLanguageCode("vi-VN")
                .build();
        QueryInput queryInput = QueryInput.newBuilder()
                .setText(textInput)
                .build();

        // Gửi request và nhận response
        DetectIntentResponse response = sessionsClient.detectIntent(
                DetectIntentRequest.newBuilder()
                        .setSession(session.toString())
                        .setQueryInput(queryInput)
                        .build()
        );

        return response.getQueryResult().getFulfillmentText();
    }

}
