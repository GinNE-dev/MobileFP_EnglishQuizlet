package com.gin.mobilefp_englishquizlet.MainMenu;

import android.content.Intent;
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

import com.gin.mobilefp_englishquizlet.AuthencationActivities.LoginActivity;
import com.gin.mobilefp_englishquizlet.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    TextView txtviewUsername;
    LinearLayout btnLogout; //test logout

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
        btnLogout = view.findViewById(R.id.btnLogout);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();

        txtviewUsername.setText(userEmail);

        //test logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getActivity(), "Logged out!", Toast.LENGTH_SHORT).show();
            goToLogIn();
        });
    }

    private void goToLogIn() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}