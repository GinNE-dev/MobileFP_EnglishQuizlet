package com.gin.mobilefp_englishquizlet.StudyMode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RequestHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultsLayout extends AppCompatActivity {
    private TextView textViewCorrect;
    private TextView textViewIncorrect;
    private TextView textViewScore;
    private RecyclerView resultsLayoutCorrectWord;
    private RecyclerView resultsLayoutIncorrectWord;
    private Button buttonRestartTest;
    private Button buttonNewTest;
    private TextView textViewFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);

        initViews();
        registerEvents();

        int correct = getIntent().getIntExtra("correct", -1);
        int incorrect = getIntent().getIntExtra("incorrect", -1);
        int score = getIntent().getIntExtra("score", -1);
        ArrayList<Word> words = getIntent().getParcelableArrayListExtra("words");
        ArrayList<Integer> answers = getIntent().getIntegerArrayListExtra("answers");

        updateUI(words, answers, correct, incorrect, score);
    }

    private void registerEvents(){
        this.buttonRestartTest.setOnClickListener(btn->{
            Intent data = new Intent();
            data.putExtra("command", RequestHelper.CommandFromResult.COMMAND_RESTART_TEST);
            setResult(RESULT_OK, data);
            finish();
        });

        this.buttonNewTest.setOnClickListener(btn->{
            Intent data = new Intent();
            data.putExtra("command", RequestHelper.CommandFromResult.COMMAND_NEW_TEST);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    private void updateUI(ArrayList<Word> words, ArrayList<Integer> answers, int correct, int incorrect, int score){
        this.textViewScore.setText(String.valueOf(score));
        this.textViewIncorrect.setText(String.valueOf(incorrect));
        this.textViewCorrect.setText(String.valueOf(correct));

        ArrayList<String> feedbacks = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.feedbacks)));
        int nPart = feedbacks.size();
        int totalQuestion = answers.size();
        int feedbackIdx = 0;
        feedbackIdx = (int) Math.min(nPart-1,(int) correct*1.0f/(totalQuestion*1.0f/nPart));
        String feedback = feedbacks.get(feedbackIdx);
        this.textViewFeedback.setText(feedback);
        
        ArrayList<Word> correctWords = new ArrayList<>();
        ArrayList<Word> incorrectWords = new ArrayList<>();

        for(int i=0; i<answers.size(); i++){
            Integer answer = answers.get(i);
            Word word = words.get(i);
            if(answer.equals(1)){
                correctWords.add(word);
            }else {
                incorrectWords.add(word);
            }
        }

        WordInResultAdapter correctWordAdapter = new WordInResultAdapter(correctWords);
        this.resultsLayoutCorrectWord.setAdapter(correctWordAdapter);
        this.resultsLayoutCorrectWord.setLayoutManager(new LinearLayoutManager(this));

        WordInResultAdapter incorrectWordAdapter = new WordInResultAdapter(incorrectWords);
        this.resultsLayoutIncorrectWord.setAdapter(incorrectWordAdapter);
        this.resultsLayoutIncorrectWord.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews(){
        this.textViewCorrect = findViewById(R.id.text_view_correct);
        this.textViewIncorrect = findViewById(R.id.text_view_incorrect);
        this.textViewScore = findViewById(R.id.text_view_score);
        this.resultsLayoutCorrectWord = findViewById(R.id.recycler_view_correct_words);
        this.resultsLayoutIncorrectWord = findViewById(R.id.recycler_view_incorrect_words);
        this.buttonRestartTest = findViewById(R.id.button_restart_test);
        this.buttonNewTest = findViewById(R.id.button_new_test);
        this.textViewFeedback = findViewById(R.id.text_view_feedback);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
