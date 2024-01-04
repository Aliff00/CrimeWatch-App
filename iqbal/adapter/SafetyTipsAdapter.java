package com.example.iqbal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iqbal.R;
import com.example.iqbal.data.SafetyTip;

import java.util.List;

public class SafetyTipsAdapter extends RecyclerView.Adapter<SafetyTipsAdapter.SafetyTipViewHolder> {

    private List<SafetyTip> safetyTips;

    public SafetyTipsAdapter(List<SafetyTip> safetyTips) {
        this.safetyTips = safetyTips;
    }

    @NonNull
    @Override
    public SafetyTipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_safety_tips, parent, false);
        return new SafetyTipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SafetyTipViewHolder holder, int position) {
        SafetyTip safetyTip = safetyTips.get(position);

        // Bind data to ViewHolder
        holder.titleTextView.setText(safetyTip.getTitle());
        holder.descriptionTextView.setText(safetyTip.getDescription());
    }

    @Override
    public int getItemCount() {
        return safetyTips.size();
    }

    // ViewHolder class
    public static class SafetyTipViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;

        public SafetyTipViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.textViewSafetyTipTitle);
            descriptionTextView = itemView.findViewById(R.id.textViewSafetyTipDescription);
        }
    }
}
