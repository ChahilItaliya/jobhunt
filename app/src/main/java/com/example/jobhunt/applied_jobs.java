package com.example.jobhunt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class applied_jobs extends Fragment {
    private RecyclerView recyclerView;
    private AppliedJobsAdapter adapter;
    private List<AppliedJob> appliedJobList;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applied_jobs, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        appliedJobList = new ArrayList<>();
        adapter = new AppliedJobsAdapter(appliedJobList);
        recyclerView.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore = FirebaseFirestore.getInstance();
            Log.d("user id", userId);
            fetchUserData(userId);
        } else {
            // Handle the case when no user is currently signed in
        }

        return view;
    }

    // Inside fetchUserData method or wherever you retrieve data from Firestore
    private void fetchUserData(String userId) {
        firestore.collection("users")
                .document(userId)
                .collection("jobApply")
                .whereEqualTo("process", "applied")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot companyDocument : task.getResult()) {
                                String jobId = companyDocument.getId();
                                String companyId = companyDocument.getString("companyId");
                                fetchApplyJobs(companyId,jobId);
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }
    private void fetchApplyJobs(String companyId, String jobId) {
        firestore.collection("jobs")
                .document(companyId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String companyName = document.getString("title");
                                String img = document.getString("img");
                                fetchJobDetails(companyId, companyName, jobId, img);
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }


    private void fetchJobDetails(String companyId,String companyName, String jobId,String img) {
        firestore.collection("jobs")
                .document(companyId)
                .collection("job")
                .document(jobId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot jobDocument = task.getResult();
                            if (jobDocument.exists()) {
//                                String companyName = jobDocument.getString("companyName");
                                String jobName = jobDocument.getString("designation");
                                AppliedJob appliedJob = new AppliedJob(companyName, jobName, img);
                                appliedJobList.add(appliedJob);
                                adapter.notifyDataSetChanged();
                            } else {
                                // Handle case where job document doesn't exist
                            }
                        } else {
                            // Handle Firestore read errors
                        }
                    }
                });
    }

    // AppliedJobsAdapter class definition here
    // Make sure to define your RecyclerView adapter class and its ViewHolder inside this class
}
