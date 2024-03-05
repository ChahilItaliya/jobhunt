package com.example.jobhunt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText dobEditText;
    private EditText mobileNumberEditText; // Added EditText field for mobile number
    private Button registerButton;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        emailEditText = findViewById(R.id.reg_email);
        passwordEditText = findViewById(R.id.reg_password);
        nameEditText = findViewById(R.id.reg_name);
        dobEditText = findViewById(R.id.reg_dob);
        mobileNumberEditText = findViewById(R.id.reg_dob); // Initialize mobileNumberEditText
        registerButton = findViewById(R.id.register);
        login = findViewById(R.id.login);

        // Set click listener for the register button
        registerButton.setOnClickListener(v -> registerUser());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim(); // Get mobile number

        // Validate email
        if (!isValidEmail(email)) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        // Validate mobile number
        if (!isValidMobileNumber(mobileNumber)) {
            mobileNumberEditText.setError("Enter a valid 10-digit mobile number");
            mobileNumberEditText.requestFocus();
            return;
        }

        // Firebase Authentication: Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            String userId = auth.getCurrentUser().getUid();
                            if (userId != null) {
                                // Firebase Firestore: Save additional user details
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("email", email);
                                user.put("dob", dob);
                                user.put("mobile", mobileNumber); // Save mobile number

                                firestore.collection("users").document(userId)
                                        .set(user)
                                        .addOnSuccessListener(aVoid ->
                                                Log.d("RegisterActivity", "User details added to Firestore"))
                                        .addOnFailureListener(e ->
                                                Log.e("RegisterActivity", "Error adding user details to Firestore", e));
                            }
                            Intent in = new Intent(RegisterActivity.this, EducationActivity.class);
                            startActivity(in);
                        } else {
                            // If registration fails, display a message to the user.
                            Log.e("RegisterActivity", "User registration failed", task.getException());
                            Toast.makeText(RegisterActivity.this, "User registration failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        // Validate if the mobile number is a 10-digit number
        return mobileNumber.length() == 10 && android.text.TextUtils.isDigitsOnly(mobileNumber);
    }
    public void onLoginClick(View view) {
        Log.d("LoginClick", "Login button clicked");
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}

