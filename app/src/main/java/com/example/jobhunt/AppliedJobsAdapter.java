package com.example.jobhunt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.AppliedJobViewHolder> {
    private List<AppliedJob> appliedJobList;

    // Constructor and methods for setting data and item click listener

    public static class AppliedJobViewHolder extends RecyclerView.ViewHolder {
        private TextView jobTitleTextView;
        private TextView companyNameTextView;

        public AppliedJobViewHolder(View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.title_text_view);
            companyNameTextView = itemView.findViewById(R.id.description_text_view);
        }
    }

    @NonNull
    @Override
    public AppliedJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
        return new AppliedJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobViewHolder holder, int position) {
        AppliedJob appliedJob = appliedJobList.get(position);
        holder.jobTitleTextView.setText(appliedJob.getJobTitle());
        holder.companyNameTextView.setText(appliedJob.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return appliedJobList.size();
    }
}
