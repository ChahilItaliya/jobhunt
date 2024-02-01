package com.example.jobhunt;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TempActivity extends AppCompatActivity {

    private TextView textView,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        textView = findViewById(R.id.temp);
        description = findViewById(R.id.description);

        String title = getIntent().getStringExtra("title");

        String des = getIntent().getStringExtra("description");

        textView.setText(title != null ? title : "No Title");
        description.setText(des != null ? des : "No description");

    }
}