package com.gin.mobilefp_englishquizlet.MainMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gin.mobilefp_englishquizlet.AuthencationActivities.LoginActivity;
import com.gin.mobilefp_englishquizlet.Library.EditTopicActivity;
import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.Profile.SettingProfileActivity;
import com.gin.mobilefp_englishquizlet.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    TextView txtviewUsername;
    CircleImageView imgAvatar;
    AppCompatImageButton btnEdit;
    LinearLayout btnSetting;
    AlertDialog nameDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtviewUsername = view.findViewById(R.id.txtviewUsername);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnEdit = view.findViewById(R.id.btnEdit);
        imgAvatar = view.findViewById(R.id.imgAvatar);

        btnEdit.setOnClickListener(v -> {
            showChangeInfoDialog();
        });

        btnSetting.setOnClickListener(v -> {
            Intent goToSetting = new Intent(getActivity(), SettingProfileActivity.class);
            startActivity(goToSetting);
        });

        setUpInfo();
    }

    private void setUpInfo() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(userID);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("name").getValue().toString();
                String userAva = snapshot.child("avaURL").getValue().toString();

                txtviewUsername.setText(userName);

                int imageSize = 100; // Size in dp
                int imageSizePixels = (int) (imageSize * getResources().getDisplayMetrics().density);
                Glide.with(getActivity())
                        .load(userAva)
                        .apply(new RequestOptions()
                                .override(imageSizePixels, imageSizePixels)
                                .centerCrop())
                        .into(imgAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showChangeInfoDialog() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_dialog_profile, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);

        // Find and set up your buttons or other views inside the bottom sheet
        Button btnChangeAva = bottomSheetView.findViewById(R.id.btnChangeAva);
        Button btnChangeName = bottomSheetView.findViewById(R.id.btnChangeName);

        btnChangeName.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showNameDialog();
        });

        bottomSheetDialog.show();
    }

    private void showNameDialog() {
        // Create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_username, null);
        builder.setView(dialogView);

        EditText edtxtName = dialogView.findViewById(R.id.edtxtName);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        edtxtName.setText(txtviewUsername.getText().toString());

        // Set OnClickListener for the Confirm button
        btnConfirm.setOnClickListener(view -> {
            String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference("users").child(userUID).child("name");

            String newName = edtxtName.getText().toString();

            boolean valid = true;
            if(newName.equals("")) {
                valid = false;
                Toast.makeText(getActivity(), "Enter your new display name!", Toast.LENGTH_SHORT).show();
            }

            if(valid) {
                usernameRef.setValue(newName);
                txtviewUsername.setText(newName);
                Toast.makeText(getActivity(), "Your display name has been updated!", Toast.LENGTH_SHORT).show();
                nameDialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        nameDialog = builder.create();
        nameDialog.show();
    }
}