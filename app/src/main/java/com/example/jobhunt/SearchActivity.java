package com.example.jobhunt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataList;
    private List<String> allTitles; // Store all titles fetched from Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();
        allTitles = new ArrayList<>(); // Initialize the list for all titles
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(dataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performLocalSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Fetch all data from Firestore initially
        fetchAllTitlesFromFirestore();
    }

    private void fetchAllTitlesFromFirestore() {
        CollectionReference collectionReference = db.collection("jobs");

        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allTitles.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String title = documentSnapshot.getString("title");
                        if (title != null) {
                            allTitles.add(title.toLowerCase()); // Store lowercase titles for case-insensitive search
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur while fetching all titles
                });
    }

    private void performLocalSearch(String searchText) {
        dataList.clear();
        if (!searchText.isEmpty()) {
            for (String title : allTitles) {
                if (title.startsWith(searchText.toLowerCase())) {
                    dataList.add(title);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
