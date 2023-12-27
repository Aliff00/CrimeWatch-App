package com.example.crimewatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {
    private List<ContactModel> dataList;

    public EmergencyAdapter(List<ContactModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_emergency_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContactModel item = dataList.get(position);
        holder.textViewTitle.setText(item.getName());
        holder.textViewPhoneNumber.setText(item.getPhoneNo());
        // Load image using a library like Picasso or Glide
        // Example using Picasso:
        // Picasso.get().load(item.getImageUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewPhoneNumber;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.TVTitleHelpline);
            imageView = itemView.findViewById(R.id.IVHelpline);
            textViewPhoneNumber = itemView.findViewById(R.id.TVPhoneHelpline);
        }
    }
}
