package com.about.kby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.about.kby.Activity.AboutusActivity;
import com.about.kby.Activity.GroupsActivity;
import com.about.kby.Activity.HomeActivity;
import com.about.kby.Activity.OpenGroupActivity;
import com.about.kby.Activity.PersonalExistingActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class Group2Activity extends AppCompatActivity {
    LinearLayout ll_personal,ll_opencircle;
    ImageView backimage,image;
    TextView txt_toolbar;
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group2);

        ll_opencircle = findViewById(R.id.ll_opencircle);
        ll_personal = findViewById(R.id.ll_personal);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        backimage = findViewById(R.id.backimage);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        image = findViewById(R.id.image);
        ll_opencircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Group2Activity.this, OpenGroupActivity.class);
                startActivity(intent);
            }
        });
        ll_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Group2Activity.this, PersonalExistingActivity.class);
                startActivity(intent);
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Group2Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Group2Activity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.infos));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.group));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.terms));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //initialize intent
                Intent intent = null;

                if (item.getId() == 4) {
                    intent = new Intent(Group2Activity.this, DateActivity.class);
                } else if (item.getId() == 2) {
                    intent = new Intent(Group2Activity.this, AboutusActivity.class);
                } else if (item.getId() == 1) {
                    intent = new Intent(Group2Activity.this, HomeActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        bottomNavigation.show(3, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(getApplicationContext()," You clicked "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

                //Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        txt_toolbar.setText("Groups");
    }

    @Override
    public void onBackPressed() {
        finish();
        bottomNavigation.show(1, true);
        super.onBackPressed();
    }
}