package com.gin.mobilefp_englishquizlet.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.gin.mobilefp_englishquizlet.AuthencationActivities.LoginActivity;
import com.gin.mobilefp_englishquizlet.Library.CreateTopicActivity;
import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Models.User;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingProfileActivity extends AppCompatActivity {
    MaterialButton btnLogout;
    MaterialButton btnChangePass;
    AppCompatImageButton btnBack;
    TextView txtviewEmail, txtviewUsername;
    AlertDialog changePassDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_profile_layout);

        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        btnChangePass = findViewById(R.id.btnChangePass);
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

        btnChangePass.setOnClickListener(v -> {
            showChangePassDialog();
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
        Log.d("VCLLLLLLLL", mAuth.getCurrentUser().getUid());
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String userEmail = mAuth.getCurrentUser().getEmail();
//                String userName = snapshot.child("name").getValue().toString();
                User user = snapshot.getValue(User.class);
                String userEmail = user.getEmail();
                String userName = user.getName();

                txtviewEmail.setText(userEmail);
                txtviewUsername.setText(userName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void showChangePassDialog() {
        // Create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        // Find the EditText fields in the dialog layout
        EditText edtxtOldPass = dialogView.findViewById(R.id.edtxtOldPass);
        EditText edtxtNewPass = dialogView.findViewById(R.id.edtxtNewPass);
        EditText edtxtNewPass2 = dialogView.findViewById(R.id.edtxtNewPass2);
        MaterialButton btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        // Set OnClickListener for the Confirm button
        btnConfirm.setOnClickListener(view -> {
            String oldPass = edtxtOldPass.getText().toString();
            String newPass = edtxtNewPass.getText().toString();
            String newPass2 = edtxtNewPass2.getText().toString();

            boolean valid = true;
            if(oldPass.equals("") || newPass.equals("")) {
                valid = false;
                Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_SHORT).show();
            }
            else if(!newPass.equals(newPass2)) {
                valid = false;
                Toast.makeText(this, "Your re-enter password is not matched", Toast.LENGTH_SHORT).show();
            }

            if(valid) {
                progressBar.setVisibility(View.VISIBLE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);

                user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SettingProfileActivity.this, "Your password has been changed!", Toast.LENGTH_SHORT).show();
                                changePassDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Reauthentication failed, old password is incorrect
                            Toast.makeText(SettingProfileActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // Other failure, handle it accordingly
                            Toast.makeText(SettingProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        // Create and show the AlertDialog
        changePassDialog = builder.create();
        changePassDialog.show();
    }

    private void updatePassword(String oldPass, String newPass) {

    }
}

