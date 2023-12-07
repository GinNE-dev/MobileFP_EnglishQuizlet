package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gin.mobilefp_englishquizlet.DetailPages.TopicDetailActivity;
import com.gin.mobilefp_englishquizlet.Models.Topic;
import com.gin.mobilefp_englishquizlet.Models.User;
import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForTopics extends RecyclerView.Adapter<AdapterForTopics.MyViewHolder>{
    Context context;
    ArrayList<Topic> topics;
    String folderID = "";
    public AdapterForTopics(Context context, ArrayList<Topic> topics) {
        //constructor
        this.context = context;
        this.topics = topics;
    }
    public AdapterForTopics(Context context, ArrayList<Topic> topics, String folderID) {
        //constructor
        this.context = context;
        this.topics = topics;
        this.folderID = folderID;
    }

    @NonNull
    @Override
    public AdapterForTopics.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topic_item, parent, false);
        return new AdapterForTopics.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterForTopics.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtviewTitle.setText(topics.get(position).getTitle());
        int termCount = topics.get(position).getWords().size();
        holder.txtviewTermCount.setText(Integer.toString(termCount));
        holder.txtviewOwner.setText(topics.get(position).getOwner());

        holder.cardView.setOnClickListener(v -> {
            Intent goToDetail = new Intent(context, TopicDetailActivity.class);
            goToDetail.putExtra("id", topics.get(position).getId());
            goToDetail.putExtra("owner", topics.get(position).getOwner());
            context.startActivity(goToDetail);

            Log.i("TOPIC ID", topics.get(position).getId());
        });

        if(!folderID.equals("")) {
            holder.cardView.setOnLongClickListener(v -> {
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(context);
                confirmBuilder.setMessage("Remove this topic from the current folder?")
                        .setPositiveButton("Remove", (dialog1, id) -> {
                            DatabaseReference topicBelongsToFoldersRef = FirebaseDatabase.getInstance().getReference("topics").child(topics.get(position).getId()).child("belongsToFolders");
                            topicBelongsToFoldersRef.child(folderID).setValue(false);

                            Toast.makeText(context, "This topic has been removed from your folder", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog1, id) -> {
                        });
                confirmBuilder.show();
                return false;
            });
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(topics.get(position).getOwner());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User currentUser = snapshot.getValue(User.class);
//                holder.txtviewOwner.setText(currentUser.getName());
                String userName = snapshot.child("name").getValue().toString();
                String userAva = snapshot.child("avaURL").getValue().toString();

                holder.txtviewOwner.setText(userName);

                Glide.with(context)
                        .load(userAva)
                        .into(holder.imgAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        //count
        return topics.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewTitle, txtviewTermCount, txtviewOwner;
        CardView cardView;
        CircleImageView imgAvatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewTitle = itemView.findViewById(R.id.txtviewTitle);
            txtviewTermCount = itemView.findViewById(R.id.txtviewTermCount);
            txtviewOwner = itemView.findViewById(R.id.txtviewOwner);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
