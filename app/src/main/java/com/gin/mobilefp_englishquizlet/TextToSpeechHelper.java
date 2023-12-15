package com.gin.mobilefp_englishquizlet;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextToSpeechHelper {

    private static TextToSpeech textToSpeech;

    public static void initialize(Context context, final OnInitializationListener listener) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        if (listener != null) {
                            listener.onInitializationFailed();
                        }
                    } else {
                        if (listener != null) {
                            listener.onInitialized();
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onInitializationFailed();
                    }
                }
            });
        } else {
            if (listener != null) {
                listener.onInitialized();
            }
        }
    }

    public static void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speech");
        }
    }

    public static void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public interface OnInitializationListener {
        void onInitialized();
        void onInitializationFailed();
    }
}