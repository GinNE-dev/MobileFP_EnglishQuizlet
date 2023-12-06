package com.gin.mobilefp_englishquizlet.Utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.gin.mobilefp_englishquizlet.R;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ResultPopup {
    public static long DURATION_SHORT = 3000;
    public static long DURATION_LONG = 5000;

    public enum PopupType{
        CorrectAnswer,
        IncorrectAnswer
    }

    public static void show(@NonNull AppCompatActivity context, @NonNull PopupType popupType, long duration){
        duration = Math.min(Math.max(DURATION_SHORT, duration), DURATION_LONG);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        if(popupType.equals(PopupType.CorrectAnswer)){
            resId = R.layout.dialog_correct;
        } else
        if (popupType.equals(PopupType.IncorrectAnswer)){
            resId = R.layout.dialog_incorrect;
        }else{}
        View layout = inflater.inflate(resId, (ViewGroup) context.findViewById(R.id.popup_container));
        PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(duration);
        fadeOut.setFillAfter(true);

        layout.startAnimation(fadeOut);

        new Handler().postDelayed(() -> {
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }, 100);

        new Handler().postDelayed(() -> popupWindow.dismiss(), duration);
    }
}
