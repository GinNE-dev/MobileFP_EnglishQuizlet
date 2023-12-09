package com.gin.mobilefp_englishquizlet;

import android.net.Uri;
import android.os.Environment;

import java.util.regex.Pattern;

public class GlobalUtil {
    public static boolean containsSpecialCharacters(String input) {
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        return special.matcher(input).find();
    }
}
