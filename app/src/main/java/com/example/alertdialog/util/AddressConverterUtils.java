package com.example.alertdialog.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddressConverterUtils {
    private static final String JSON_FILE_PATH = "/sdcard/address_min.json"; // JSON 文件路径
    private static final Map<String, String> areaCodeMap = new HashMap<>();

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File(JSON_FILE_PATH);
            JsonNode areaData = objectMapper.readTree(jsonFile);
            parseAreaData(areaData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseAreaData(JsonNode areaData) {
        for (JsonNode province : areaData) {
            String provinceCode = province.get("code").asText();
            for (JsonNode city : province.get("children")) {
                String cityCode = city.get("code").asText();
                for (JsonNode district : city.get("children")) {
                    String districtCode = district.get("code").asText();
                    String districtName = district.get("name").asText();
                    areaCodeMap.put(districtName, districtCode);
                }
            }
        }
    }

    public static String convertToAreaCode(String addressName) {
        return areaCodeMap.getOrDefault(addressName, "no exist!!");
    }

}



