package com.example.jobhunt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.search.SearchBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView,recyclerViewc;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private CardAdapter cardAdapter;
    private List<Card> cardList;
    private BottomNavigationView bottomNavigationView;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private CollectionReference jobsRef;
    private CollectionReference cardRef;
    private SearchBar searchBar;
    private DrawerLayout drawerLayout;
    private ImageView profileImageView;


//    SearchView searchView = findViewById(R.id.searchView);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        String userId = auth.getCurrentUser().getUid();

        fetchUserName(userId);


        drawerLayout = findViewById(R.id.drawer_layout);
        profileImageView = findViewById(R.id.userPhoto);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewc = findViewById(R.id.recyclerviewc);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Display the default activity
        //startActivity(new Intent(MainActivity.this, MainActivity.class));


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);



        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(jobList);
        recyclerView.setAdapter(jobAdapter);

        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList);
        recyclerViewc.setAdapter(cardAdapter);
        recyclerViewc.setLayoutManager(new LinearLayoutManager(this));

        cardAdapter.listener = new CardAdapter.onCardClicked() {
            @Override
            public void onCardClicked(Card card) {
                // Handle the click action here, for example, start a new activity with the card details
                Data data = new Data();
                data.setId(card.getDocumentId());
                //Log.d("ItemCount", "Total items Card: " + card.getTitle());
                Log.d("ItemCount", "Total items Card: " + card.getTitle());
                Log.d("ItemCount", "Total items id: " + card.getDocumentId());
                Intent intent = new Intent(MainActivity.this, TempActivity.class);
                intent.putExtra("title", card.getTitle()); // Pass the card ID to the new activity
                intent.putExtra("description", card.getDescription());
                startActivity(intent);
            }
        };

        jobAdapter.listener = new JobAdapter.OnJobClickListener() {
            @Override
            public void onJobClicked(Job job) {
                Data data = new Data();
                data.setId(job.getDocumentId());
                Log.d("ItemCount", "Total items Card: " + job.getTitle());
                Log.d("ItemCount", "Total items id: " + job.getDocumentId());
                Intent intent = new Intent(MainActivity.this, TempActivity.class);
                intent.putExtra("title", job.getTitle()); // Pass the card ID to the new activity
                intent.putExtra("description", job.getDescription());
                startActivity(intent);

            }
        };
        ImageButton btnOpenSidebar = findViewById(R.id.btnOpenSidebar);
        btnOpenSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the sidebar
                openSidebar();
            }
            private void openSidebar() {
                drawerLayout.openDrawer(GravityCompat.START);
            }

            private void closeSidebar() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        });


        SearchBar searchBar = findViewById(R.id.search_bar);
        searchBar.setOnClickListener(view -> {
            Intent in = new Intent(MainActivity.this,SearchActivity.class);
            startActivity(in);
        });
//
//        searchView.setupWithSearchBar(searchBar);


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        jobsRef = db.collection("jobs");
        cardRef = db.collection("jobs");

        // Retrieve data from Firestore
        jobsRef.orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            String title = document.getString("title");
                            String description = document.getString("description");


                            Job job = new Job(title, description,documentId);
                            jobList.add(job);

                        }
                        // Notify adapter after loading all data outside the loop
                        jobAdapter.notifyDataSetChanged();


                        // After loading data into your jobList, log its size to check the total count
                        Log.d("ItemCount", "Total items: " + jobList.size());


                    } else {
                        // Handle error
                    }
                });

        // Retrieve data from Firestore
        cardRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            String title = document.getString("title");
                            String description = document.getString("description");
                            Card card = new Card(title, description, documentId);
                            cardList.add(card);
                        }
                        // Notify adapter after loading all data outside the loop
                        cardAdapter.setCardList(cardList);
                        cardAdapter.notifyDataSetChanged();
                        // After loading data into your jobList, log its size to check the total count
                        Log.d("ItemCount", "Total items Card: " + cardList.size());
                    } else {
                        // Handle error
                    }
                });

        }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.home) {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        // Handle home action if needed
                    } else if (item.getItemId() == R.id.resume) {
                        startActivity(new Intent(MainActivity.this, ResumeActivity.class));
                    } else if (item.getItemId() == R.id.star) {
                        startActivity(new Intent(MainActivity.this, EducationActivity.class));
                    } else if (item.getItemId() == R.id.edu) {
                        startActivity(new Intent(MainActivity.this, InterstActivity.class));
                    }
                    return true;
                }
            };

    private void fetchUserName(String userId) {
        // Get the current user ID
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
//            String userId = currentUser.getUid();

            // Reference to the user document in Firestore
            DocumentReference userDocRef = db.collection("users").document(userId);

            // Fetch the user document
            userDocRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the "name" field from the user document
                            String imgurl = documentSnapshot.getString("profileImageUrl");
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");

                            Data data = new Data();
                            data.setPhotourl(imgurl);

                            // Do something with the user's name (e.g., display in a TextView)
                            if (imgurl != null) {
                                loadProfileImage(imgurl);
                                loadNavHeaderImage(imgurl,name,email);
                                Log.d("Mainavitivy", "img url"+imgurl);
                            }
                        } else {
                            Log.d("InterstActivity", "User document does not exist");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("InterstActivity", "Error fetching user document: " + e.getMessage());
                    });
        }
    }
    private void loadProfileImage(String imageURL) {
        if (imageURL != null && !imageURL.isEmpty()) {
            // Use an image-loading library like Glide to load and display the image
            // Replace R.drawable.default_profile with your default placeholder image
            Glide.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_warning_24)
                    .into(profileImageView);
        } else {
            // If no image URL is available, you can set a default placeholder image here
            profileImageView.setImageResource(R.drawable.baseline_warning_24);
        }
    }
    private void loadNavHeaderImage(String imageURL,String txtname,String txtemail) {
        // Reference to the NavigationView header ImageView
        NavigationView navigationView = findViewById(R.id.navbar1);
        ImageView navHeaderImageView = navigationView.getHeaderView(0).findViewById(R.id.image_view);
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.txtname);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.txtemail);

        name.setText(txtname);
        email.setText(txtemail);

        if (imageURL != null && !imageURL.isEmpty()) {
            // Use an image-loading library like Glide to load and display the image
            Glide.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.new1removebg)
                    .error(R.drawable.baseline_warning_24)
                    .into(navHeaderImageView);
        } else {
            // If no image URL is available, you can set a default placeholder image here
            navHeaderImageView.setImageResource(R.drawable.new1removebg);
        }
    }

}
