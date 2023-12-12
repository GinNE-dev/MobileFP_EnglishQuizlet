package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gin.mobilefp_englishquizlet.DetailPages.FolderDetailActivity;
import com.gin.mobilefp_englishquizlet.Models.Record;
import com.gin.mobilefp_englishquizlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForRecords extends RecyclerView.Adapter<AdapterForRecords.MyViewHolder>{
    Context context;
    ArrayList<Record> records;
    public AdapterForRecords(Context context, ArrayList<Record> records) {
        //constructor
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public AdapterForRecords.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ranking_item, parent, false);
        return new AdapterForRecords.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterForRecords.MyViewHolder holder, int position) {
        holder.txtviewScore.setText(Integer.toString(records.get(position).getScore()));

        double minute = records.get(position).getTimeConsumed() / 1000.0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String minuteString = decimalFormat.format(minute);

        holder.txtviewTime.setText(minuteString + " seconds");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(records.get(position).getArchivedBy());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("name").getValue().toString();
                String userAva = snapshot.child("avaURL").getValue().toString();

                holder.txtviewUsername.setText(userName);

                Glide.with(context)
                        .load(userAva)
                        .into(holder.imgUserAva);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        //count
        return records.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewUsername, txtviewScore, txtviewTime;
        CircleImageView imgUserAva;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewUsername = itemView.findViewById(R.id.txtviewUsername);
            txtviewScore = itemView.findViewById(R.id.txtviewScore);
            txtviewTime = itemView.findViewById(R.id.txtviewTime);
            imgUserAva = itemView.findViewById(R.id.imgUserAva);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
