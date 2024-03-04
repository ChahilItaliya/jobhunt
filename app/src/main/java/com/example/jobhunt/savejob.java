package com.example.jobhunt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class savejob extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savejob);

        ViewPager viewPager = findViewById(R.id.view_pager);
        MaterialButtonToggleGroup toggleButton = findViewById(R.id.toggleButton);

        // Create an instance of FragmentPagerAdapter
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                // Return the appropriate fragment based on the position
                if (position == 0) {
                    return new applied_jobs();
                } else if (position == 1) {
                    return new approved_jobs();
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                // Return the number of pages
                return 2;
            }
        };

        // Set the adapter for the ViewPager
        viewPager.setAdapter(pagerAdapter);

        // Set default checked state to "Apply Job" button
        toggleButton.check(R.id.button1);

        // Set up toggle button listener
        toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                // Update ViewPager when button checked
                if (isChecked) {
                    int position = toggleButton.indexOfChild(findViewById(checkedId));
                    viewPager.setCurrentItem(position);
                }
                for (int i = 0; i < group.getChildCount(); i++) {
                    MaterialButton btn = (MaterialButton) group.getChildAt(i);
                    if (btn.getId() != checkedId) {
                        group.uncheck(btn.getId());
                    }
                }
            }
        });
    }
}