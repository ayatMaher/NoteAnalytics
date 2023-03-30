package com.example.noteanalyticsapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteanalyticsapplication.R;
import com.example.noteanalyticsapplication.model.Note;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final List<Note> mData;
    private final LayoutInflater mInflater;
    private final ItemClickListener mClickListener;

    public NoteAdapter(Context context, List<Note> data, ItemClickListener onClick) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = onClick;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.note_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(mData.get(position).getName());
        holder.description.setText(mData.get(position).getDescription());
        Picasso.get().load(mData.get(position).getImage()).fit().centerInside().into(holder.imageView);
        holder.container.setOnClickListener(v -> mClickListener.onItemClick(holder.getAdapterPosition(), mData.get(position).getNoteId()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView description;
        public TextView name;
        public ImageView imageView;
        public ConstraintLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            this.description = itemView.findViewById(R.id.txtDescription);
            this.name = itemView.findViewById(R.id.txtName);
            this.imageView = itemView.findViewById(R.id.noteImage);
            this.container = itemView.findViewById(R.id.containerNote);

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
