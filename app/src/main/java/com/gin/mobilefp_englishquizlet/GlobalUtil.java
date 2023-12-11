package com.gin.mobilefp_englishquizlet;

import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class GlobalUtil {
    public static boolean containsSpecialCharacters(String input) {
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        return special.matcher(input).find();
    }

    public static void writeFileToDownloadDirectory(String fileContent, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        FileOutputStream fos = null;
        try {
            // Instantiate the FileOutputStream object and pass myExternalFile in constructor
            fos = new FileOutputStream(file);
            // Write to the file
            fos.write(fileContent.getBytes());
            // Close the stream
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
