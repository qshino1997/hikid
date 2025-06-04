package com.example.service.WebhookDialogFlowService;

import com.google.gson.JsonObject;

public interface WebhookProcessor {
    JsonObject process(JsonObject body);

    JsonObject buildAskManufacturer(JsonObject body, String manufacturerName);

    JsonObject buildGetProductDetail(JsonObject body, String chosenName);

    JsonObject buildAddToCart(JsonObject params);
}
