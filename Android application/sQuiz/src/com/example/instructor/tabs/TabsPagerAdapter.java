package com.example.instructor.tabs;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public ListFragment getItem(int index) {
        switch (index) {
        case 0:
            return new InstructorGroupFragment();
        case 1:
            return new InstructorQuizzFragment();
        }
        
        return null;
    }
 
    @Override
    public int getCount() {
        return 2;
    }
 
}