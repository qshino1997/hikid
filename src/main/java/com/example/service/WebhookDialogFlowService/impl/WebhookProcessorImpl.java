package com.example.service.WebhookDialogFlowService.impl;

import com.example.dto.ProductDto;
import com.example.service.WebhookDialogFlowService.ProductWebhookService;
import com.example.service.WebhookDialogFlowService.WebhookProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class WebhookProcessorImpl implements WebhookProcessor {
    @Autowired
    private ProductWebhookService productWebhookService;

    /**
     * Nhận vào 1 JsonObject giống payload mà Dialogflow gửi
     * và trả về JsonObject chứa { fulfillmentText, outputContexts? }.
     */
    @Override
    public JsonObject process(JsonObject body) {
        // Lấy intentName
        String intentName = body
                .getAsJsonObject("queryResult")
                .getAsJsonObject("intent")
                .get("displayName")
                .getAsString();

        // Lấy params
        JsonObject params = body
                .getAsJsonObject("queryResult")
                .getAsJsonObject("parameters");

        String manufacturerName = extractStringParam(params, "manufacturer");
        String productName      = extractStringParam(params, "product");
        JsonObject jsonResponse = switch (intentName) {
            case "ask_manufacturer" -> buildAskManufacturer(body, manufacturerName);
            case "getProductDetailFromManufacturer" -> buildGetProductDetail(body, productName);
            case "AddToCart" -> buildAddToCart(body);
            default -> buildFallback();
        };

        return jsonResponse;
    }

    @Override
    public JsonObject buildAskManufacturer(JsonObject body, String manufacturerName) {
        // ... (copy nguyên logic từ handleAskManufacturer, chỉ trả JsonObject)
        // Ví dụ:
        JsonObject result = new JsonObject();
        String replyText;
        if (manufacturerName.isEmpty()) {
            replyText = "Bạn muốn hỏi thương hiệu nào?";
            result.addProperty("fulfillmentText", replyText);
            return result;
        }
        List<ProductDto> list = productWebhookService.findManufacturerName(manufacturerName);
        if (list == null || list.isEmpty()) {
            replyText = "Hiện tại bên em chưa có sản phẩm nào cho thương hiệu " + manufacturerName + ".";
            result.addProperty("fulfillmentText", replyText);
            return result;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Hiện tại bên em đang có các sản phẩm ").append(manufacturerName).append(":\n");
        int limit = Math.min(3, list.size());
        for (int i = 0; i < limit; i++) {
            sb.append(i+1).append(". ").append(list.get(i).getName()).append("\n");
        }
        if (list.size() > 3) {
            sb.append("…và ").append(list.size() - 3).append(" sản phẩm khác.");
        }
        sb.append("\nAnh/Chị muốn em cung cấp thông tin về sản phẩm nào ạ");
        replyText = sb.toString().trim();

        // Build outputContexts
        String sessionFull = body.get("session").getAsString();
        String contextName = sessionFull + "/contexts/awaiting_product_choice";
        JsonObject ctx = new JsonObject();
        ctx.addProperty("name", contextName);
        ctx.addProperty("lifespanCount", 1);
        JsonArray arr = new JsonArray();
        arr.add(ctx);

        result.addProperty("fulfillmentText", replyText);
        result.add("outputContexts", arr);
        return result;
    }

    @Override
    public JsonObject buildGetProductDetail(JsonObject body, String chosenName) {
        JsonObject result = new JsonObject();
        String replyText;

        if (chosenName.isEmpty()) {
            replyText = "Anh/Chị vui lòng cho em biết tên sản phẩm cụ thể ạ.";
            result.addProperty("fulfillmentText", replyText);
            return result;
        }
        ProductDto product = productWebhookService.findByName(chosenName);
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));

        if (product != null) {
            // Build replyText chi tiết
            String price = nf.format(product.getPrice()) + "đ";
            String ageRange = (product.getAppropriate_age_start() != null && product.getAppropriate_age_end() != null)
                    ? product.getAppropriate_age_start() + " - " + product.getAppropriate_age_end() + " tháng"
                    : "Không xác định";
            String warning = (product.getWarning() != null && !product.getWarning().isEmpty())
                    ? product.getWarning() : "Không có cảnh báo.";
            String origin = product.getMade_in();
            String weight = (product.getProduct_weight() != null)
                    ? product.getProduct_weight() + "g" : "Không rõ";
            replyText = String.format(
                    "Thông tin chi tiết về %s:\n- Giá: %s\n- Độ tuổi phù hợp: %s\n- Cảnh báo: %s\n- Xuất xứ: %s\n- Khối lượng: %s",
                    product.getName(), price, ageRange, warning, origin, weight
            );

            // Tạo context awaiting_add_to_cart
            String sessionFull = body.get("session").getAsString();
            String contextName = sessionFull + "/contexts/awaiting_add_to_cart";

            JsonObject ctx = new JsonObject();
            ctx.addProperty("name", contextName);
            ctx.addProperty("lifespanCount", 2);

            JsonObject paramsObj = new JsonObject();
            paramsObj.addProperty("product_id", product.getProduct_id());
            ctx.add("parameters", paramsObj);

            JsonArray arr = new JsonArray();
            arr.add(ctx);

            result.addProperty("fulfillmentText", replyText);
            result.add("outputContexts", arr);

            System.out.println("=== OUTPUT CONTEXTS FROM buildGetProductDetail ===");
            System.out.println(result.get("outputContexts").toString());

            return result;
        } else {
            replyText = "Xin lỗi, em không tìm thấy sản phẩm nào có tên “" + chosenName + "”.";
            result.addProperty("fulfillmentText", replyText);
            return result;
        }
    }

    @Override
    public JsonObject buildAddToCart(JsonObject body) {
        JsonObject result = new JsonObject();
        String replyText;

        // 1. Lấy phần queryResult
        JsonObject queryResult = body.getAsJsonObject("queryResult");

        // 2. Lấy mảng outputContexts nếu có, nếu không thì tạo mới mảng rỗng
        JsonArray outputContexts = queryResult.has("outputContexts")
                ? queryResult.getAsJsonArray("outputContexts")
                : new JsonArray();

        // 3. Quét qua từng context để tìm đúng "awaiting_add_to_cart"
        String productIdStr = "";
        for (int i = 0; i < outputContexts.size(); i++) {
            JsonObject ctx = outputContexts.get(i).getAsJsonObject();
            String ctxName = ctx.has("name") ? ctx.get("name").getAsString() : "";

            // Chỉ lấy context có tên kết thúc bằng "/contexts/awaiting_add_to_cart"
            if (ctxName.endsWith("/contexts/awaiting_add_to_cart")) {
                JsonObject paramsOfContext = ctx.has("parameters")
                        ? ctx.getAsJsonObject("parameters")
                        : null;

                if (paramsOfContext != null && paramsOfContext.has("product_id")
                        && !paramsOfContext.get("product_id").isJsonNull()) {
                    productIdStr = paramsOfContext.get("product_id").getAsString().trim();
                }
                break;
            }
        }

        // 4. Nếu không tìm thấy product_id trong context ấy => trả lỗi
        if (productIdStr.isEmpty()) {
            replyText = "Xin lỗi, em chưa biết sản phẩm nào để thêm vào giỏ. Anh/Chị thử lại.";
            result.addProperty("fulfillmentText", replyText);
            return result;
        }

        // 5. Chuyển productIdStr thành số
        int productId;
        try {
            double d = Double.parseDouble(productIdStr);
            productId = (int) d;
        } catch (NumberFormatException e) {
            replyText = "Mã sản phẩm không hợp lệ. Vui lòng thử lại.";
            result.addProperty("fulfillmentText", replyText);
            return result;
        }

        // 6. Gọi service để thêm vào giỏ
        replyText = "Đã thêm sản phẩm vào giỏ hàng thành công! " + productId;
        result.addProperty("fulfillmentText", replyText);

        return result;
    }

    private JsonObject buildFallback() {
        JsonObject result = new JsonObject();
        result.addProperty("fulfillmentText", "Em chưa hiểu ý, anh/chị vui lòng diễn đạt lại giúp em nhé.");
        return result;
    }

    private String extractStringParam(JsonObject params, String key) {
        if (params.has(key) && !params.get(key).isJsonNull()) {
            return params.get(key).getAsString().trim();
        }
        return "";
    }
}

