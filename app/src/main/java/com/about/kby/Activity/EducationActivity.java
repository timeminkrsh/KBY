package com.about.kby.Activity;

import static com.about.kby.fragment.EducationInputFragment.todayDateStr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.about.kby.DateActivity;
import com.about.kby.R;
import com.about.kby.fragment.EducationInputFragment;
import com.about.kby.fragment.EducationInsightsFragment;
import com.about.kby.fragment.PersonalInsightsFragment;

public class EducationActivity extends AppCompatActivity {
    ViewPager viewpager;
    ImageView backimage,image;
    String group_id,groupname,admin;
    Button ed_input,ed_insight;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        viewpager =  findViewById(R.id.viewpager);
        ed_input =  findViewById(R.id.ed_input);
        ed_insight =  findViewById(R.id.ed_insight);
        backimage =  findViewById(R.id.backimage);
        image =  findViewById(R.id.image);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group_id = extras.getString("grp_id");
            groupname = extras.getString("name");
            admin =  extras.getString("admin");
        }
        System.out.println("group_id=="+group_id);
        System.out.println("group_id=="+groupname);
        Fragmentadapter adapter = new Fragmentadapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        ed_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewpager.setCurrentItem(0);

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
                    if (fragment instanceof EducationInsightsFragment) {
                        ((EducationInsightsFragment) fragment).educationinsight(todayDateStr);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            // Other required methods (onPageScrolled, onPageScrollStateChanged)
        });

        ed_insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
                refreshInsightsFragment();
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationActivity.this, EducationExistingActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });

    }

    private void refreshInsightsFragment() {
        int currentItem = viewpager.getCurrentItem();
        if (currentItem == 1) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    "android:switcher:" + R.id.viewpager + ":1");
            if (fragment instanceof EducationInsightsFragment) {
               // ((EducationInsightsFragment) fragment).educationinsight(todayDateStr);
            }
        }
    }


    public class Fragmentadapter extends FragmentPagerAdapter {
        public Fragmentadapter(FragmentManager fm) {
            super(fm);
            group_id = group_id;
            groupname = groupname;
            admin = admin;

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                System.out.println("param3=="+admin);
                return EducationInputFragment.newInstance(group_id,groupname,admin);
            } else {
                System.out.println("param3=="+admin);
                return EducationInsightsFragment.newInstance(group_id,groupname,admin);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EducationActivity.this, EducationExistingActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}