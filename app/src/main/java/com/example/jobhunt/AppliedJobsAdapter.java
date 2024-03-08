package com.example.jobhunt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.JobViewHolder> {
    private List<AppliedJob> appliedJobs;

    public AppliedJobsAdapter(List<AppliedJob> appliedJobs) {
        this.appliedJobs = appliedJobs;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        AppliedJob appliedJob = appliedJobs.get(position);
        holder.bind(holder, appliedJob);
    }

    @Override
    public int getItemCount() {
        return appliedJobs.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewJobName,textViewCompneyName;
        private ImageView img;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJobName = itemView.findViewById(R.id.title_text_view);
            textViewCompneyName = itemView.findViewById(R.id.description_text_view);
            img = itemView.findViewById(R.id.photo);
        }

        public void bind(@NonNull JobViewHolder holder, AppliedJob appliedJob) {
            textViewJobName.setText(appliedJob.getJobTitle());
            textViewCompneyName.setText(appliedJob.getCompanyName());

            Glide.with(holder.itemView.getContext())
                    .load(appliedJob.getImg())
                    .into(holder.img);
        }
    }
}
