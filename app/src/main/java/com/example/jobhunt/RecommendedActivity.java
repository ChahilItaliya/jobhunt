package com.example.jobhunt;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class RecommendedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecommendedAdapter adapter;
    private List<Recommend_job> recommendJobList;
    private FirebaseFirestore firestore;
    private List<String> selectedChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recommendJobList = new ArrayList<>();
        adapter = new RecommendedAdapter(recommendJobList);
        recyclerView.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore = FirebaseFirestore.getInstance();
            Log.d("user id", userId);
            fetchSelectedChips(userId);
        } else {
            // Handle the case when no user is currently signed in
        }
    }

    // Fetch selected chips data from Firestore
    private void fetchSelectedChips(String userId) {
        firestore.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Object selectedChipsObj = document.get("selectedChips");
                                if (selectedChipsObj instanceof String) {
                                    // Handle single string value
                                    selectedChips = new ArrayList<>();
                                    selectedChips.add((String) selectedChipsObj);
                                } else if (selectedChipsObj instanceof List) {
                                    // Handle list of strings
                                    selectedChips = (List<String>) selectedChipsObj;
                                    Log.d("selectedChipsObj" , String.valueOf(selectedChipsObj));
                                } else {
                                    // Handle unexpected data type or null value
                                    selectedChips = new ArrayList<>();
                                }
                                fetchJobs();
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }



    // Fetch all jobs from Firestore
    private void fetchJobs() {
        firestore.collection("jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot companyDocument : task.getResult()) {
                                String companyId = companyDocument.getId();
                                String companyName = companyDocument.getString("title");
                                String img = companyDocument.getString("img");
                                fetchCompanyJobs(companyId,companyName,img);
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }

    // Fetch jobs of a specific company
    private void fetchCompanyJobs(String companyId, String companyName, String img) {
        firestore.collection("jobs")
                .document(companyId)
                .collection("job")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot jobDocument : task.getResult()) {
                                Object designationObj = jobDocument.get("designation");
                                if (designationObj instanceof String) {
                                    Log.d("designationObj" , String.valueOf(designationObj));
                                    // Handle single string value
                                    String designation = (String) designationObj;
                                    if (selectedChips.contains(designation)) {
                                        String jobTitle = jobDocument.getString("designation");
                                        Recommend_job recommend_job = new Recommend_job(companyName, jobTitle, img);
                                        recommendJobList.add(recommend_job);
                                    }
                                } else if (designationObj instanceof List) {
                                    // Handle list of strings
                                    List<String> designations = (List<String>) designationObj;
                                    for (String designation : designations) {
                                        if (selectedChips.contains(designation)) {
//                                            String companyName = jobDocument.getString("designation");
                                            String jobTitle = jobDocument.getString("designation");
                                            Recommend_job recommend_job = new Recommend_job(companyName, jobTitle, img);
                                            recommendJobList.add(recommend_job);
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                });
    }
}
