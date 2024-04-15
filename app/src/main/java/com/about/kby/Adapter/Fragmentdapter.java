package com.about.kby.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.about.kby.fragment.PersonalInputFragment;
import com.about.kby.fragment.PersonalInsightsFragment;

public class Fragmentdapter extends FragmentPagerAdapter {
        public Fragmentdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PersonalInputFragment();
            } else {
                return new PersonalInsightsFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

