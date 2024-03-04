package com.example.jobhunt;

import static android.app.ProgressDialog.show;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class applied_jobs extends Fragment {

    private TextView jobApplyIdTextView;
    private TextView applyJobIdTextView;
    private TextView designationTextView;

    private FirebaseAuth mAuth;

    public applied_jobs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applied_jobs, container, false);

        jobApplyIdTextView = view.findViewById(R.id.txt1);
        applyJobIdTextView = view.findViewById(R.id.txt2);
        designationTextView = view.findViewById(R.id.txt3);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        handleUserLogin();
    }

    private void handleUserLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            fetchAllJobApplyData(userID);
        } else {
            clearUI();
        }
    }

    private void clearUI() {
        jobApplyIdTextView.setText("");
        applyJobIdTextView.setText("");
        designationTextView.setText("");
    }

    private void fetchAllJobApplyData(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobApplyCollection = db.collection("users").document(userID).collection("jobApply");

        jobApplyCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot jobApplyDocument : task.getResult()) {
                    String jobApplyId = jobApplyDocument.getId();
                    String jobApplyIdText = "JobApply ID: " + jobApplyId + "\n";
                    jobApplyIdTextView.append(jobApplyIdText);

                    fetchApplyJobData(userID, jobApplyId);
                }
            } else {
                Log.d("fetchAllJobApplyData", "Error getting documents: ", task.getException());
            }
        });
    }



    private void fetchApplyJobData(String userID, String jobApplyId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference applyJobCollection = db.collection("users")
                .document(userID)
                .collection("jobApply")
                .document(jobApplyId)
                .collection("applyjob");

        applyJobCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot applyJobDocument : task.getResult()) {
                    String applyJobId = applyJobDocument.getId();
                    Log.d("ApplyJobId", "ApplyJob ID: " + applyJobId);
                    String applyJobIdText = "ApplyJob ID: " + applyJobId + "\n";
                    applyJobIdTextView.append(applyJobIdText);

                    fetchDesignationData(userID, jobApplyId, applyJobId);
                }
            } else {
                // Handle errors
            }
        });
    }

    private void fetchDesignationData(String userID, String jobApplyId, String applyJobId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobsCollection = db.collection("users")
                .document(userID)
                .collection("jobApply")
                .document(jobApplyId)
                .collection("applyjob")
                .document(applyJobId)
                .collection("job");

        jobsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot jobDocument : task.getResult()) {
                    String designation = jobDocument.getString("designation");
                    designationTextView.append("Designation: " + designation + "\n");
                }
            } else {
                // Handle errors
            }
        });
    }
}
