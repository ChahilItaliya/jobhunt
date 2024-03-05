package com.example.jobhunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppliedJobsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppliedJobsAdapter adapter;
    private List<AppliedJob> appliedJobsList;

    private FirebaseFirestore db;

    public AppliedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applied_jobs, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appliedJobsList = new ArrayList<>();
        adapter = new AppliedJobAdapter(getContext(), appliedJobsList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAppliedJobs();
    }

    private void loadAppliedJobs() {
        String userId = ""; // Provide the user ID here

        CollectionReference userJobApplyRef = db.collection("users").document(userId).collection("jobApply");

        userJobApplyRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String companyId = document.getId();
                    getJobsForCompany(companyId);
                }
            } else {
                Toast.makeText(getContext(), "Failed to fetch job applications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJobsForCompany(String companyId) {
        CollectionReference companyApplyJobRef = db.collection("companies").document(companyId).collection("applyjob");

        companyApplyJobRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String jobId = document.getId();
                    getJobDetails(companyId, jobId);
                }
            } else {
                Toast.makeText(getContext(), "Failed to fetch jobs for company", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJobDetails(String companyId, String jobId) {
        DocumentReference jobRef = db.collection("jobs").document(companyId).collection("job").document(jobId);

        jobRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String jobTitle = document.getString("title");
                    // Add job details to the list
                    AppliedJob appliedJob = new AppliedJob(jobId, jobTitle, companyId);
                    appliedJobsList.add(appliedJob);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Job details not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to fetch job details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
