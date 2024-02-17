package com.example.jobhunt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class TempActivity extends AppCompatActivity {

    private TextView textView,description,txtworkplace,txttime,txtsalary,txtlocation,txtexpr;
    ImageView imgphoto,imageView;
    Button btnapply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        textView = findViewById(R.id.temp);
        description = findViewById(R.id.description);
        txtworkplace = findViewById(R.id.workplace);
        txttime = findViewById(R.id.time);
        txtsalary = findViewById(R.id.salary);
        imgphoto = findViewById(R.id.photo);
        txtlocation = findViewById(R.id.location);
        txtexpr = findViewById(R.id.expr);
        btnapply = findViewById(R.id.apply);
        imageView = findViewById(R.id.imageView);

        String title = getIntent().getStringExtra("title");

        String des = getIntent().getStringExtra("description");
        String photo = getIntent().getStringExtra("img");


        textView.setText(title != null ? title : "No Title");
        description.setText(des != null ? des : "No description");

            Glide.with(this)
                    .load(photo)
                    .placeholder(R.drawable.new1removebg)
                    .error(R.drawable.baseline_warning_24)
                    .into(imgphoto);

        String documentId = getIntent().getStringExtra("id");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("jobs").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Document exists, extract data and display
                                String workplace = document.getString("workplace");
                                String time = document.getString("time");
                                String salary = document.getString("salary");
                                String location = document.getString("location");
                                String expr = document.getString("expr");

                                txtworkplace.setText(workplace != null ? workplace : "No workplace");
                                txttime.setText(time != null ? time : "No time");
                                txtsalary.setText(salary != null ? salary : "No salary");
                                txtlocation.setText(location != null ? location : "No location");
                                txtexpr.setText(expr != null ? expr : "No expr");

                            } else {
                                // Document does not exist, display default message
                                txtworkplace.setText("txtworkplace not found");
                                txttime.setText("No description txttime");
                                txtsalary.setText("No description txtsalary");
                            }
                        } else {
                            // Handle errors while fetching document
                            txtworkplace.setText("Error fetching txtworkplace");
                            txttime.setText("Error fetching txttime");
                            txtsalary.setText("No txtsalary available");
                        }
                    }
                });

        //image view chang
        AlreadyApplied(documentId);
        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfAlreadyApplied(documentId);
                bottomsheet bottomsheet = new bottomsheet();
                bottomsheet.show(getSupportFragmentManager(),bottomsheet.getTag());
            }
        });

    }
    private void AlreadyApplied(String jobId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId)
                    .collection("jobApply").document(jobId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // User has already applied for this job
                    imageView.setImageResource(R.drawable.baseline_bookmark);
                } else {
                    // User has not applied for this job yet
//                    applyForJob(jobId);
                }
            }).addOnFailureListener(e -> {
                // Handle failure
                imageView.setImageResource(R.drawable.baseline_bookmark_border_24);
//                Toast.makeText(this, "Failed to check if already applied", Toast.LENGTH_SHORT).show();
            });
        }
    }
    private void checkIfAlreadyApplied(String jobId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId)
                    .collection("jobApply").document(jobId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // User has already applied for this job
                    Toast.makeText(this, "You have already applied for this job", Toast.LENGTH_SHORT).show();
                } else {
                    // User has not applied for this job yet
                    applyForJob(jobId);
                }
            }).addOnFailureListener(e -> {
                // Handle failure
                Toast.makeText(this, "Failed to check if already applied", Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void applyForJob(String jobId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = db.collection("users").document(userId)
                    .collection("jobApply").document(jobId); // Use jobId as the document ID
            // Add the job ID as a field in the document within the "jobApply" subcollection
            userRef.set(new HashMap<String, Object>() {{
                        put("jobId", jobId);
                    }})
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Applied successfully", Toast.LENGTH_SHORT).show();
                        // Document created successfully
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(this, "Failed to apply", Toast.LENGTH_SHORT).show();
                    });
        }
    }

}

