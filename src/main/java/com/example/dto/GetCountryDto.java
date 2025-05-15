package com.example.dto;

import java.util.Map;

public abstract class GetCountryDto {
    private static final Map<String,String> COUNTRY_NAMES = Map.of(
            "VN", "Việt Nam",
            "CN", "Trung Quốc",
            "US", "Hoa Kỳ",
            "JP", "Nhật Bản"
    );

    public String getCountryFull(String code) {
        return COUNTRY_NAMES.getOrDefault(code, code);
    }
}
