package com.gin.mobilefp_englishquizlet.AuthencationActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.gin.mobilefp_englishquizlet.MainMenu.MainActivity;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {
    TextInputEditText edtxtEmail;
    MaterialButton btnSend;
    AppCompatImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        //check if user is already login or not
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            goToMain();
        }

        edtxtEmail = findViewById(R.id.edtxtEmail);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnSend.setOnClickListener(v -> {
            String emailAddress = edtxtEmail.getText().toString();
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPassActivity.this, "We have sent an email to your address", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}