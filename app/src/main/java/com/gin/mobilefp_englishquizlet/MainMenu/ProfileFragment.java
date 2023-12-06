package com.gin.mobilefp_englishquizlet.MainMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gin.mobilefp_englishquizlet.AuthencationActivities.LoginActivity;
import com.gin.mobilefp_englishquizlet.Profile.SettingProfileActivity;
import com.gin.mobilefp_englishquizlet.R;
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
    LinearLayout btnSetting;

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
        imgAvatar = view.findViewById(R.id.imgAvatar);

        btnSetting.setOnClickListener(v -> {
            Intent goToSetting = new Intent(getActivity(), SettingProfileActivity.class);
            startActivity(goToSetting);
        });

        setUpInfo();
    }

    private void setUpInfo() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(userID);
        user.addValueEventListener(new ValueEventListener() {
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

}