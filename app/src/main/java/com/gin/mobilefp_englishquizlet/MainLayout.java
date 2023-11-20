package com.gin.mobilefp_englishquizlet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.Login.LoginLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLayout extends AppCompatActivity {
    AppCompatImageButton btnNotification;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //check if user is already login or not
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            goToLogIn();
        }

        btnNotification = findViewById(R.id.btnNotification);

        String userEmail = mAuth.getCurrentUser().getEmail(); //get current user email
        //Toast.makeText(MainLayout.this, userEmail, Toast.LENGTH_LONG).show();

        //tam thoi de btnNotification la nut dang xuat de test
        btnNotification.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
            goToLogIn();
        });
    }

    private void goToLogIn() {
        Intent intent = new Intent(this, LoginLayout.class);
        startActivity(intent);
        finish();
    }
}
