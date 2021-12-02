package com.contactmanagementsystem.project.service;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

public final class CountryToPhonePrefix {
    public static Map<String, String> COUNTRY_CODE = MapUtils.putAll(new HashMap<String, String>(), new String[][]{
            {"AC", "+247"},
            {"AD", "+376"},
            {"GB", "+44"},
            {"IN", "+91"},
            {"US", "+1"}

    });

    public static String prefixCode(String code) {
        String result = COUNTRY_CODE.get(code);
        if (result == null) {
            throw new IllegalArgumentException("Unknown country code " + code);
        }
        return result;
    }
}
