package com.coffee.shop.starbux.cart.helpers;

import java.util.HashMap;
import java.util.Map;

public class HeaderHelper {

    public HeaderHelper() {
    }

    public static Map<String, String> addParams(
            final String name){

        Map<String, String> headers = new HashMap<>();
        headers.put("name", name);
        return headers;
    }
}
