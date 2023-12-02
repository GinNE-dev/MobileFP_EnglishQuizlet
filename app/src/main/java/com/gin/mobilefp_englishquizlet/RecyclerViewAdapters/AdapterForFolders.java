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
import com.gin.mobilefp_englishquizlet.DetailPages.TopicDetailActivity;
import com.gin.mobilefp_englishquizlet.Models.Folder;
import com.gin.mobilefp_englishquizlet.R;

import java.util.ArrayList;

public class AdapterForFolders extends RecyclerView.Adapter<AdapterForFolders.MyViewHolder> {
    Context context;
    ArrayList<Folder> folders;
    public AdapterForFolders(Context context, ArrayList<Folder> folders) {
        //constructor
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @Override
    public AdapterForFolders.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.folder_item, parent, false);
        return new AdapterForFolders.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterForFolders.MyViewHolder holder, int position) {
        holder.txtviewTitle.setText(folders.get(position).getTitle());

        holder.cardView.setOnClickListener(v -> {
            Intent goToDetail = new Intent(context, FolderDetailActivity.class);
            goToDetail.putExtra("id", folders.get(position).getId());
            goToDetail.putExtra("title", folders.get(position).getTitle());
            context.startActivity(goToDetail);

            Log.i("FOLDER ID", folders.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        //count
        return folders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewTitle;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewTitle = itemView.findViewById(R.id.txtviewTitle);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
