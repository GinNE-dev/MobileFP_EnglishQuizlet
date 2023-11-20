package com.gin.mobilefp_englishquizlet.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gin.mobilefp_englishquizlet.MainLayout;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginLayout extends AppCompatActivity {
    TextInputEditText edtxtEmail;
    TextInputEditText edtxtPassword;
    MaterialButton btnLogin;
    MaterialButton btnSignup;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //check if user is already login or not
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            goToMain();
        }

        edtxtEmail = findViewById(R.id.edtxtEmail);
        edtxtPassword = findViewById(R.id.edtxtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> {
            String loginEmail = edtxtEmail.getText().toString();
            String loginPassword = edtxtPassword.getText().toString();

            if(loginEmail.equals("") || loginPassword.equals("")) {
                Toast.makeText(this, "Please enter email or password!", Toast.LENGTH_SHORT).show();
            }
            else {
                logIn(loginEmail, loginPassword);
            }
        });

        btnSignup.setOnClickListener(v -> {
            Intent goToSignUp = new Intent(LoginLayout.this, SignUpLayout.class);
            startActivity(goToSignUp);
        });
    }

    private void logIn(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(LoginLayout.this, "Login success!", Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();

                goToMain();
            }
            else {
                progressBar.setVisibility(View.INVISIBLE);

                Exception exception = task.getException();
                if(exception instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginLayout.this, "Email or password is wrong!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginLayout.this, "Login failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainLayout.class);
        startActivity(intent);
        finish();
    }
}
