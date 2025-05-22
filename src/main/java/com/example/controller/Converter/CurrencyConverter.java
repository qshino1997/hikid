package com.example.controller.Converter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    public static BigDecimal convertVNDToUSD(BigDecimal vndAmount) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.exchangerate-api.com/v4/latest/VND";
            String response = restTemplate.getForObject(url, String.class);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonObject rates = jsonObject.getAsJsonObject("rates");
            BigDecimal usdRate = rates.get("USD").getAsBigDecimal();

            return vndAmount.multiply(usdRate).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
