package com.example.noteanalyticsapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteanalyticsapplication.R;
import com.example.noteanalyticsapplication.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private final List<Category> mData;
    private final LayoutInflater inflater;
    private final ItemClickListener itemClickListener;
    public CategoryAdapter(Context context, List<Category> data, ItemClickListener onClick){
        this.inflater=LayoutInflater.from(context);
        this.mData=data;
        this.itemClickListener=onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(mData.get(position).getName());
        holder.container.setOnClickListener(v -> itemClickListener.onItemClick(holder.getAdapterPosition(),mData.get(position).getCategoryId()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
         LinearLayout container;
        ViewHolder(View itemView){
             super(itemView);
             this.name=itemView.findViewById(R.id.txtTitle);
             this.container=itemView.findViewById(R.id.container);
             itemView.setOnClickListener(this);
         }

        @Override
        public void onClick(View v) {

        }
    }
    public interface ItemClickListener {
        void onItemClick(int position, String id);
    }
}
