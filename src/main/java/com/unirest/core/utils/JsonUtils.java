package com.unirest.core.utils;

import org.json.JSONObject;

public class JsonUtils {

    public static boolean hasKeys(JSONObject jsonObject, String... keys) {
        if (keys == null || keys.length == 0) return false;
        for (String key : keys) {
            if (!jsonObject.has(key)) {
                return false;
            }
        }
        return true;
    }

}
