package com.example.crimewatch;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.MyViewHolder> {
    private List<Report> reportList;

    public ArchiveAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_archive_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Report item = reportList.get(position);
        holder.textViewReportTitle.setText(item.getTitle());
        holder.textViewReportType.setText(item.getType());
        holder.textViewReportDate.setText(item.getDate());
        holder.textViewReportTime.setText(item.getTime());
        holder.textViewReportLocation.setText(item.getLocation());
        holder.textViewReportStatus.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReportTitle;
        TextView textViewReportType;
        TextView textViewReportDate;
        TextView textViewReportTime;
        TextView textViewReportLocation;
        TextView textViewReportStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReportTitle = itemView.findViewById(R.id.TVReportTitle);
            textViewReportType = itemView.findViewById(R.id.TVReportType);
            textViewReportDate = itemView.findViewById(R.id.TVReportDate);
            textViewReportTime = itemView.findViewById(R.id.TVReportTime);
            textViewReportLocation = itemView.findViewById(R.id.TVReportLocation);
            textViewReportStatus = itemView.findViewById(R.id.TVReportStatus);

        }
    }
}