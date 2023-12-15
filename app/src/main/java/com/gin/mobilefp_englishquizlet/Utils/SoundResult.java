package com.gin.mobilefp_englishquizlet.Utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.gin.mobilefp_englishquizlet.R;

import java.util.Date;
import java.util.Random;

public class SoundResult {
    public static enum Type{
        Correct,
        Incorrect
    }
    private static MediaPlayer correctSoundMediaPlayer;
    private static MediaPlayer incorrectSoundMediaPlayer;
    public static void playSoundEffect(Context context, SoundResult.Type soundType){
        if(Type.Correct.equals(soundType)){
            /*
            if(correctSoundMediaPlayer == null){
                correctSoundMediaPlayer = MediaPlayer.create(context, R.raw.correct_answer_sound);
            }

            if(correctSoundMediaPlayer.isPlaying()){
                correctSoundMediaPlayer.stop();
                correctSoundMediaPlayer = MediaPlayer.create(context, R.raw.correct_answer_sound);
            }
            */
            correctSoundMediaPlayer = MediaPlayer.create(context, R.raw.correct_answer_sound);
            correctSoundMediaPlayer.start();
        } else
        if (Type.Incorrect.equals(soundType)) {
            /*
            if(incorrectSoundMediaPlayer == null){
                incorrectSoundMediaPlayer = MediaPlayer.create(context, R.raw.incorrect_answer_sound);
            }

            if(incorrectSoundMediaPlayer.isPlaying()){
                incorrectSoundMediaPlayer.stop();
                incorrectSoundMediaPlayer = MediaPlayer.create(context, R.raw.incorrect_answer_sound);
            }
            */
            incorrectSoundMediaPlayer = MediaPlayer.create(context, /*Math.abs(new Date().getTime())%2 == 0 ? R.raw.sound_effect :*/ R.raw.incorrect_answer_sound);
            incorrectSoundMediaPlayer.start();
        }

    }
}
