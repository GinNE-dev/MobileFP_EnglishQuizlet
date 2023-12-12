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

import com.gin.mobilefp_englishquizlet.DetailPages.FolderDetailActivity;
import com.gin.mobilefp_englishquizlet.Models.Record;
import com.gin.mobilefp_englishquizlet.R;

import java.util.ArrayList;

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
        holder.txtviewTime.setText(Long.toString(records.get(position).getTimeConsumed()));
        holder.txtviewUsername.setText(records.get(position).getArchivedBy());
    }

    @Override
    public int getItemCount() {
        //count
        return records.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewUsername, txtviewScore, txtviewTime;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewUsername = itemView.findViewById(R.id.txtviewUsername);
            txtviewScore = itemView.findViewById(R.id.txtviewScore);
            txtviewTime = itemView.findViewById(R.id.txtviewTime);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
