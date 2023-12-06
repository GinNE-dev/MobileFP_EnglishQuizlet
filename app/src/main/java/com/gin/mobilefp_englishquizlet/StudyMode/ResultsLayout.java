package com.gin.mobilefp_englishquizlet.StudyMode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.RequestHelper;

import java.util.ArrayList;

public class ResultsLayout extends AppCompatActivity {
    private TextView textViewCorrect;
    private TextView textViewIncorrect;
    private TextView textViewScore;
    private RecyclerView resultsLayoutCorrectWord;
    private RecyclerView resultsLayoutIncorrectWord;
    private Button buttonRestartTest;
    private Button buttonNewTest;

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
        ArrayList<String> answers = getIntent().getStringArrayListExtra("answers");
        boolean isRevert = getIntent().getBooleanExtra("is_revert", false);

        updateUI(words, answers, isRevert, correct, incorrect, score);
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

    private void updateUI(ArrayList<Word> words, ArrayList<String> answers, boolean isRevert, int correct, int incorrect, int score){
        this.textViewScore.setText(String.valueOf(score));
        this.textViewIncorrect.setText(String.valueOf(incorrect));
        this.textViewCorrect.setText(String.valueOf(correct));

        ArrayList<Word> correctWords = new ArrayList<>();
        ArrayList<Word> incorrectWords = new ArrayList<>();


        for(int i=0; i<answers.size(); i++){
            String answer = answers.get(i);
            Word word = words.get(i);
            if(isRevert){
                if(word.getTerm().equals(answer)){
                    correctWords.add(word);
                }else {
                    incorrectWords.add(word);
                }
            }else{
                if(word.getDefinition().equals(answer)){
                    correctWords.add(word);
                }else {
                    incorrectWords.add(word);
                }
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
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
