package com.gin.mobilefp_englishquizlet.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.Models.Word;
import com.gin.mobilefp_englishquizlet.R;
import com.gin.mobilefp_englishquizlet.TextToSpeechHelper;

import java.util.ArrayList;

public class AdapterForEditableWords extends RecyclerView.Adapter<AdapterForEditableWords.MyViewHolder>{
    Context context;
    ArrayList<Word> words;
    public AdapterForEditableWords(Context context, ArrayList<Word> words) {
        //constructor
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Word currentWord = words.get(position);

        holder.txtviewTerm.setText(currentWord.getTerm());
        holder.txtviewDefinition.setText(currentWord.getDefinition());
        if(!currentWord.getDescription().equals("")) {
            holder.txtviewDescription.setText(currentWord.getDescription());
        }
        else {
            holder.txtviewDescription.setText("None");
        }

        holder.cardView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose your action")
                    .setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index of the selected item
                            switch (which) {
                                case 0:
                                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_word, null);
                                    AlertDialog.Builder builderEdit = new AlertDialog.Builder(context);
                                    builderEdit.setView(dialogView);
                                    // Find the EditText fields in the dialog layout
                                    EditText edtxtTerm = dialogView.findViewById(R.id.edtxtTerm);
                                    EditText edtxtDefinition = dialogView.findViewById(R.id.edtxtDefinition);
                                    EditText edtxtDescription = dialogView.findViewById(R.id.edtxtDescription);
                                    Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

                                    edtxtTerm.setText(currentWord.getTerm());
                                    edtxtTerm.setEnabled(false);
                                    edtxtTerm.setFocusable(false);
                                    edtxtTerm.setFocusableInTouchMode(false);
                                    edtxtDefinition.setText(currentWord.getDefinition());
                                    edtxtDescription.setText(currentWord.getDescription());

                                    AlertDialog alertDialog = builderEdit.create();

                                    btnConfirm.setOnClickListener(view -> {
                                        currentWord.setDefinition(edtxtDefinition.getText().toString());
                                        currentWord.setDescription(edtxtDescription.getText().toString());
                                        Toast.makeText(context, "Saved change!", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                        alertDialog.dismiss();
                                    });

                                    alertDialog.show();
                                    break;
                                case 1:
                                    AlertDialog.Builder builderRemove = new AlertDialog.Builder(context);
                                    builderRemove.setTitle("Remove this word?")
                                            .setMessage("Are you sure you want to remove this word?")
                                            .setPositiveButton("Yes", (dialog0, which0) -> {
                                                words.remove(position);
                                                notifyItemRemoved(position);
                                            })
                                            .setNegativeButton("No", (dialog0, which0) -> {
                                                dialog0.dismiss();
                                            })
                                            .show();
                                    break;
                            }
                        }
                    });

            builder.show();
        });

    }
    @Override
    public int getItemCount() {
        //count
        return words.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtviewTerm, txtviewDefinition, txtviewDescription;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtviewTerm = itemView.findViewById(R.id.txtviewTerm);
            txtviewDefinition = itemView.findViewById(R.id.txtviewDefinition);
            txtviewDescription = itemView.findViewById(R.id.txtviewDescription);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
