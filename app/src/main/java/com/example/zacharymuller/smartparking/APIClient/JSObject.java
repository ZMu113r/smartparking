package com.example.zacharymuller.smartparking.APIClient;

import java.util.HashMap;

public class JSObject {
    private HashMap<String, String> attribs;

    public JSObject(String[] keys, String[] vals) {
        attribs = new HashMap<String, String>();

        for (int i = 0; i < keys.length; i++) {
            attribs.put(keys[i], vals[i]);
        }
    }

    public String toString() {
        String s = "{\n";
        for (String key : attribs.keySet()) {
            if (key.startsWith("_")) continue;
            s += key + ": " + attribs.get(key) + ",\n";
        }
        s += "}";

        return s;
    }

    public String get(String query) {
        return attribs.get(query);
    }
}
