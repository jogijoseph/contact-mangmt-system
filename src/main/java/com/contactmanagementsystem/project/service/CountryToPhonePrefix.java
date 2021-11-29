package com.contactmanagementsystem.project.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class CountryToPhonePrefix {

    private static Map<String, String> map = new HashMap<>();

    public String prefixCode(String code) {
        String result = map.get(code);
        if (result == null) {
            throw new IllegalArgumentException("Unknown country code " + code);
        }
        return result;
    }
    static {
        map.put("AC", "+247");
        map.put("AD", "+376");
        map.put("GB", "+44");
        map.put("IN", "+91");
        map.put("US", "+1");
    }
}