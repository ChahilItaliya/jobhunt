package com.example.jobhunt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class savejob extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Card> cardList;
    private RecyclerView recyclerView;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savejob);

        recyclerView = findViewById(R.id.recyclerviewc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardList = new ArrayList<>();
        adapter = new CardAdapter(cardList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Assuming you have the document ID from Firestore passed through intent extras
        String documentId = getIntent().getStringExtra("documentId");

        if (documentId != null) {
            // Retrieve data from the "jobs" collection using the documentId
            db.collection("jobs").document(documentId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                // Retrieve data from the document
                                String title = documentSnapshot.getString("title");
                                String description = documentSnapshot.getString("description");
                                String photoResource = documentSnapshot.getString("photoResource");

                                // Check if photoResource is not null or empty before loading with Glide
                                if (photoResource != null && !photoResource.isEmpty()) {
                                    Card card = new Card(title, description, documentId, photoResource);
                                    cardList.add(card);

                                    // Notify the adapter of the data change
                                    adapter.notifyDataSetChanged();
                                } else {
                                    // Handle the case where the photoResource is null or empty
                                    // You can set a default image or handle it according to your requirements
                                }
                            } else {
                                // Document does not exist
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                        }
                    });
        } else {
            // Handle the case when documentId is null
        }
    }
}