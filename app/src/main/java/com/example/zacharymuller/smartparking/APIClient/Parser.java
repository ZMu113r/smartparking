package com.example.zacharymuller.smartparking.APIClient;

import java.util.ArrayList;

public class Parser {
    public static JSObject parse(String s) {
        JSObject finalObj;
        String jsString = s.substring(2);
        String[] attribs = jsString.split(",");
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> vals = new ArrayList<String>();

        for (String attrib : attribs) {
            String[] pieces = attrib.split(":");
            keys.add(pieces[0].replaceAll("\"", ""));
            vals.add(pieces[1].replaceAll("\"", ""));
        }

        String[] keyArray = keys.toArray(new String[keys.size()]);
        String[] valArray = vals.toArray(new String[vals.size()]);

        finalObj = new JSObject(keyArray, valArray);
        return finalObj;
    }
}
