package com.gin.mobilefp_englishquizlet.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.AuthencationActivities.LoginActivity;
import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Models.User;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingProfileActivity extends AppCompatActivity {
    MaterialButton btnLogout;
    AppCompatImageButton btnBack;
    TextView txtviewEmail, txtviewUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_profile_layout);

        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        txtviewEmail = findViewById(R.id.txtviewEmail);
        txtviewUsername = findViewById(R.id.txtviewUsername);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
            goToLogIn();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        setUpInfo();
    }

    private void goToLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpInfo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userEmail = mAuth.getCurrentUser().getEmail();
                String userName = snapshot.child("name").getValue().toString();

//                Log.i("hey", snapshot.getKey().toString());
//                User user = snapshot.getValue(User.class);
//                String userEmail = user.getEmail();
//                String userName = user.getName();

                txtviewEmail.setText(userEmail);
                txtviewUsername.setText(userName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}
