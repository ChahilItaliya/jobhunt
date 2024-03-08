package com.example.jobhunt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bottomsheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bottomsheet extends BottomSheetDialogFragment {


    public bottomsheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jobId Parameter 1.

     * @return A new instance of fragment bottomsheet.
     */
    // TODO: Rename and change types and number of parameters
    public static bottomsheet newInstance(String companyId,String jobId) {
        bottomsheet fragment = new bottomsheet();
        Bundle args = new Bundle();
//        args.putString("resume",resume);
        args.putString("jobId",jobId);// Add the ID to the arguments bundle
        args.putString("companyId",companyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);

        // Retrieve references to views
        TextView textViewName = view.findViewById(R.id.txtresume);
        TextView textViewMobileNumber = view.findViewById(R.id.txtnumber);
        Button button = view.findViewById(R.id.submit);
//        String resume = getArguments().getString("resume");
        String companyId = getArguments().getString("companyId");
        String jobId = getArguments().getString("jobId");

        // Set the text name
        //textViewName.setText(jobId);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve data from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Retrieve resume file name
                    String resumeFileName = documentSnapshot.getString("resumeFileName");
                    if (resumeFileName != null) {
                        textViewName.setText(resumeFileName);
                    } else {
                        // Handle case where resume file name is not found
                    }

                    // Retrieve mobile number
                    String mobileNumber = documentSnapshot.getString("dob");
                    if (mobileNumber != null) {
                        String mobileNumberWithHint = "Mobile No : " + mobileNumber;
                        textViewMobileNumber.setText(mobileNumberWithHint);
                    } else {
                        // Handle case where mobile number is not found
                    }
                } else {
                    // Handle case where user document does not exist
                }
            }).addOnFailureListener(e -> {
                // Handle failure to retrieve data
            });
        }
        // Set click listener on the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event here
                // For example, you can dismiss the bottom sheet dialog
                Log.d("companyId" , companyId);
                Log.d("jobId" , jobId);
                applyForJob(companyId,jobId);

            }
        });

        return view;
    }
    private void applyForJob(String companyId, String jobId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Store the user's application under the company's job listing
            DocumentReference companyJobRef = db.collection("jobs")
                    .document(companyId)
                    .collection("job")
                    .document(jobId)
                    .collection("candidates")
                    .document(userId);

            // Update a "process" field in the user's job application entry
            DocumentReference userJobRef = db.collection("users")
                    .document(userId)
                    .collection("jobApply")
                    .document(jobId);  // Note: Assuming jobId is unique across users

            HashMap<String, Object> userData = new HashMap<>();
            userData.put("companyId", companyId);
            userData.put("jobId", jobId);
            userData.put("process","applied");

            HashMap<String, Object> jobData = new HashMap<>();
            jobData.put("userId", userId);
            jobData.put("process", "applied");

            // Start a transaction to ensure consistency of data
            db.runTransaction((Transaction.Function<Void>) transaction -> {
                        transaction.set(companyJobRef, jobData); // Store user's application under company's job
                        transaction.set(userJobRef, userData); // Update user's job application entry
                        return null;
                    })
                    .addOnSuccessListener(aVoid -> {
                        // Application successful
                        Log.d("ApplyForJob", "Applied successfully");
                        dismiss();
                        // Handle UI changes or any other actions upon successful application
                    })
                    .addOnFailureListener(e -> {
                        // Application failed
                        Log.e("ApplyForJob", "Failed to apply", e);
                        // Handle any errors or notify the user about the failure
                    });
        }
    }


}