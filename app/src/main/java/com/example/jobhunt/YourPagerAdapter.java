package com.example.jobhunt;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class YourPagerAdapter extends FragmentPagerAdapter {

    public YourPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                return new DesrcFragment();
            case 1:
                //return new DesrcFragment();
            case 2:
                //return new DesrcFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // Number of tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set tab titles here
        switch (position) {
            case 0:
                return "Description";
            case 1:
                return "About Us";
            case 2:
                return "Contact Us";
            default:
                return null;
        }
    }
}
