package com.about.kby.Activity;

import static com.about.kby.fragment.PersonalInputFragment.selectedDateStr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.about.kby.DateActivity;
import com.about.kby.fragment.ChecklistInputFragment;
import com.about.kby.fragment.ChecklistInsightsFragment;
import com.about.kby.R;
import com.about.kby.fragment.PersonalInsightsFragment;

public class ChecklistActivity extends AppCompatActivity {
    ViewPager viewpager;
    Button input,insight;
    ImageView backimage,image;
    TextView txt_toolbar;
    String task_id="",tack_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        viewpager =  findViewById(R.id.viewpager);
        input =  findViewById(R.id.input);
        insight =  findViewById(R.id.insight);
        backimage =  findViewById(R.id.backimage);
        image =  findViewById(R.id.image);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            task_id = extras.getString("task_id");
            tack_name = extras.getString("tack_name");
            txt_toolbar.setText(tack_name);
            System.out.println("task_id="+task_id);
        }
        Fragmentadapter adapter = new Fragmentadapter(getSupportFragmentManager(),task_id,tack_name);
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
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistActivity.this, HomeActivity.class);
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
                    if (fragment instanceof ChecklistInsightsFragment) {
                        ((ChecklistInsightsFragment) fragment).name_list();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            // Other required methods (onPageScrolled, onPageScrollStateChanged)
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });

    }
    private void refreshInsightsFragment() {
        int currentItem = viewpager.getCurrentItem();
        if (currentItem == 1) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    "android:switcher:" + R.id.viewpager + ":1");
            if (fragment instanceof ChecklistInsightsFragment) {
               ((ChecklistInsightsFragment) fragment).name_list();
            }
        }
    }

    public class Fragmentadapter extends FragmentPagerAdapter {
        private String task_id,tack_name;

        public Fragmentadapter(FragmentManager fm, String task_id,String tack_name) {
            super(fm);
            this.task_id = task_id;
            this.tack_name = tack_name;

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                ChecklistInputFragment fragment = new ChecklistInputFragment();
                Bundle bundle = new Bundle();
                bundle.putString("task_id", task_id);
                bundle.putString("tack_name", tack_name);
                fragment.setArguments(bundle);
                return fragment;
            }
            else {
                ChecklistInsightsFragment fragment = new ChecklistInsightsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("task_id", task_id);
                bundle.putString("tack_name", tack_name);
                fragment.setArguments(bundle);
                return fragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    @Override
    public void onBackPressed() {
      finish();
        super.onBackPressed();
    }
}