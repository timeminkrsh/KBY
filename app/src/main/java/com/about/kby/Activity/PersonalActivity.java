package com.about.kby.Activity;

import static com.about.kby.fragment.PersonalInputFragment.selectedDateStr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.about.kby.Adapter.Fragmentdapter;
import com.about.kby.DateActivity;
import com.about.kby.R;
import com.about.kby.fragment.ChecklistInsightsFragment;
import com.about.kby.fragment.PersonalInputFragment;
import com.about.kby.fragment.PersonalInsightsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PersonalActivity extends AppCompatActivity {

        ViewPager viewpager;
        Button input,insight;
        ImageView backimage,image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        viewpager =  findViewById(R.id.viewpager);
        insight =  findViewById(R.id.insight);
        backimage =  findViewById(R.id.backimage);
        image =  findViewById(R.id.image);
        input =  findViewById(R.id.input);
        Fragmentdapter adapter = new Fragmentdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(0);
            }
        });
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewpager.setCurrentItem(1);
                refreshInsightsFragment();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) { // Assuming PersonalInsightsFragment is at position 1
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                            "android:switcher:" + R.id.viewpager + ":1");
                    if (fragment instanceof PersonalInsightsFragment) {
                        ((PersonalInsightsFragment) fragment).personal_user_input_fetch(selectedDateStr);
                        System.out.println("selectedDateStr=="+selectedDateStr);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            // Other required methods (onPageScrolled, onPageScrollStateChanged)
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    private void refreshInsightsFragment() {
        int currentItem = viewpager.getCurrentItem();
        if (currentItem == 1) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    "android:switcher:" + R.id.viewpager + ":1");
            if (fragment instanceof PersonalInsightsFragment) {
               ((PersonalInsightsFragment) fragment).personal_user_input_fetch(selectedDateStr);
            }
        }
    }

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PersonalActivity.this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}