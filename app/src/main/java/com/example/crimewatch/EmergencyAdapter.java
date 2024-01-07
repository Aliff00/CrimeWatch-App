package com.example.crimewatch;

import android.annotation.SuppressLint;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {
    private static List<ContactModel> dataList;
    private static OnItemClickListener onItemClickListener;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(ContactModel item);
    }

    public EmergencyAdapter(List<ContactModel> dataList, OnItemClickListener onItemClickListener) {

        this.dataList = dataList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_emergency_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactModel item = dataList.get(position);
        holder.textViewTitle.setText(item.getName());
        holder.textViewPhoneNumber.setText(item.getPhoneNo());
        holder.textViewDesc.setText(item.getDesc());
        holder.imageView.setImageResource(item.getImageResourceId());
        final boolean isExpanded = position==mExpandedPosition;
        holder.hiddenView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = isExpanded ? -1:position;
//                notifyItemChanged(previousExpandedPosition);
//                notifyItemChanged(position);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewPhoneNumber;
        ImageView imageView;
        LinearLayout hiddenView;

        TextView textViewDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.TVTitleHelpline);
            imageView = itemView.findViewById(R.id.IVHelpline);
            textViewPhoneNumber = itemView.findViewById(R.id.TVPhoneHelpline);
            hiddenView = itemView.findViewById(R.id.hiddenView);
            textViewDesc = itemView.findViewById(R.id.TVDescHelpline);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(dataList.get(position));
                    }
                }
            });
        }
    }
}
