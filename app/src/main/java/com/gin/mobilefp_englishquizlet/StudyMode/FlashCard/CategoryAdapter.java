package com.gin.mobilefp_englishquizlet.StudyMode.FlashCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gin.mobilefp_englishquizlet.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context mContext;
    private List<Category> mListCategory;

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Category> list){
        this.mListCategory = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.recyclerViewWordItem.setLayoutManager(linearLayoutManager);

        WordItemTopicAdapter wordItemTopicAdapter = new WordItemTopicAdapter();
        wordItemTopicAdapter.setData(category.getWordItemTopics());
        holder.recyclerViewWordItem.setAdapter(wordItemTopicAdapter);
    }

    @Override
    public int getItemCount() {
        return mListCategory.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        RecyclerView recyclerViewWordItem;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerViewWordItem = itemView.findViewById(R.id.recyclerview_word_item);
        }
    }
}
