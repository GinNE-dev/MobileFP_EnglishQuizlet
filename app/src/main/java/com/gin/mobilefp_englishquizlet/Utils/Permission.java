package com.gin.mobilefp_englishquizlet.Utils;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class Permission {
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static ActivityResultLauncher<String> createPermissionAsker(ActivityResultCaller caller,
                                                                       PermissionGrantedCallback handler){
        return caller.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        handler.handleGrantedPermissionTask();
                    }
                });
    }
}
