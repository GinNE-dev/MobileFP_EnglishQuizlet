package com.gin.mobilefp_englishquizlet.AuthencationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.MainMenu.MainActivity;
import com.gin.mobilefp_englishquizlet.Models.User;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    AppCompatImageButton btnBack;
    MaterialButton btnSignup;
    TextInputEditText edtxtUsername;
    TextInputEditText edtxtEmail;
    TextInputEditText edtxtPassword;
    TextInputEditText edtxtPassword2;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_layout);

        //check if user is already login or not
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            goToMain();
        }

        edtxtUsername = findViewById(R.id.edtxtUsername);
        edtxtEmail = findViewById(R.id.edtxtEmail);
        edtxtPassword = findViewById(R.id.edtxtPassword);
        edtxtPassword2 = findViewById(R.id.edtxtPassword2);
        btnBack = findViewById(R.id.btnBack);
        btnSignup = findViewById(R.id.btnSignup);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnSignup.setOnClickListener(v -> {
            String email = edtxtEmail.getText().toString().trim();
            String name = edtxtUsername.getText().toString().trim();
            String password = edtxtPassword.getText().toString();
            String password2 = edtxtPassword2.getText().toString();

            boolean isFormValid = true;
            if(name.equals("") || email.equals("") || password.equals("") || password2.equals("")) {
                isFormValid = false;
                Toast.makeText(SignupActivity.this, "Please fill out the form!", Toast.LENGTH_SHORT).show();
            }
            else if(!password.equals(password2)) {
                isFormValid = false;
                Toast.makeText(SignupActivity.this, "Your re-enter password is not match!", Toast.LENGTH_SHORT).show();
            }
            else if(containsSpecialCharacters(name)) {
                isFormValid = false;
                Toast.makeText(SignupActivity.this, "Your name cannot contains numbers or special characters!", Toast.LENGTH_SHORT).show();
            }

            if(isFormValid) {
                signUp(email, password, name);
            }
        });
    }

    public void signUp(String email, String pass, String name) {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                userRef.child(userID).setValue(new User(userID, user.getEmail(), name));

                Toast.makeText(SignupActivity.this, "Your account has been created successfully!", Toast.LENGTH_SHORT).show();

                goToMain();
            }
            else {
                progressBar.setVisibility(View.INVISIBLE);

                Exception exception = task.getException();
                if(exception instanceof FirebaseAuthWeakPasswordException) {
                    Toast.makeText(SignupActivity.this, "Password is too weak!", Toast.LENGTH_SHORT).show();
                }
                else if(exception instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(SignupActivity.this, "Email address is invalid!", Toast.LENGTH_SHORT).show();
                }
                else if(exception instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(SignupActivity.this, "This email is unavailable!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignupActivity.this, "Signup failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean containsSpecialCharacters(String input) {
        // Use Pattern and Matcher to check for special characters
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        return special.matcher(input).find();
    }
}