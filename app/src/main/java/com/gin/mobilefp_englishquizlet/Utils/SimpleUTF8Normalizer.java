package com.gin.mobilefp_englishquizlet.Utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SimpleUTF8Normalizer {
    public static String normalize(String inputString){
        String result = "";
        try {
            String temp = Normalizer.normalize(inputString, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            result = pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static boolean equals(String s1, String s2){
        if(s1 == null || s2 == null) return false; //NOTE
        return normalize(s1).equals(normalize(s2));
    }
}