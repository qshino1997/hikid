package com.example.dto.chatbotDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String response;
    private Integer qty;

    public ChatResponse(String response){
        this.response = response;
    }
}
