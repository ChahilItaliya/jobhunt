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

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.JobViewHolder> {
    private List<Recommend_job> recommendJobs;

    public RecommendedAdapter(List<Recommend_job> recommendJobs) {
        this.recommendJobs = recommendJobs;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Recommend_job recommendJob = recommendJobs.get(position);
        holder.bind(recommendJob);
    }

    @Override
    public int getItemCount() {
        return recommendJobs.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewJobTitle, textViewCompanyName;
        private ImageView img;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJobTitle = itemView.findViewById(R.id.title_text_view);
            textViewCompanyName = itemView.findViewById(R.id.description_text_view);
            img = itemView.findViewById(R.id.photo);
        }

        public void bind(Recommend_job recommendJob) {
            textViewJobTitle.setText(recommendJob.getJobTitle());
            textViewCompanyName.setText(recommendJob.getCompanyName());

            Glide.with(itemView.getContext())
                    .load(recommendJob.getImg()) // Assuming Recommend_job has a method to get image URL
                    .into(img);
        }
    }
}
