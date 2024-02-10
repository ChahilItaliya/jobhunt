package com.example.jobhunt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecentjobActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecentAdapter recentAdapter;
    List<Job> recentJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentjob);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize recentJobs list
        recentJobs = new ArrayList<>();

        // Create recent adapter
        recentAdapter = new RecentAdapter(recentJobs);
        recyclerView.setAdapter(recentAdapter);

        // Fetch data from Firestore
        fetchRecentJobsFromFirestore();
    }

    private void fetchRecentJobsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobsRef = db.collection("jobs");

        jobsRef.orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String title = document.getString("title");
                                String description = document.getString("description");
                                String photo = document.getString("img");

                                Job job = new Job(title, description, documentId, photo);
                                recentJobs.add(job);
                            }
                            // Notify adapter after loading all data
                            recentAdapter.notifyDataSetChanged();
                        } else {
                            // Handle error
                            Toast.makeText(RecentjobActivity.this, "Failed to fetch jobs: " + task.getException(), Toast.LENGTH_SHORT).show();
                            Log.e("RecentjobActivity", "Error fetching jobs", task.getException());
                        }
                    }
                });
        recentAdapter.setListener(new RecentAdapter.OnJobClickListener() {
            @Override
            public void onJobClicked(Job job) {
                Data data = new Data();
                data.setId(job.getDocumentId());
                Log.d("ItemCount", "Total items Card: " + job.getTitle());
                Log.d("ItemCount", "Total items id: " + job.getDocumentId());
                Intent intent = new Intent(RecentjobActivity.this, TempActivity.class);
                intent.putExtra("title", job.getTitle()); // Pass the card ID to the new activity
                intent.putExtra("description", job.getDescription());
                intent.putExtra("id", job.getDocumentId());
                intent.putExtra("img", job.getPhoto());
                startActivity(intent);
            }
        });
    }
}
