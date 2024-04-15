package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.about.kby.DateActivity;
import com.about.kby.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class SupportActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView image,backimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        bottomNavigation=findViewById(R.id.bottomNavigation);
        image=findViewById(R.id.image);
        backimage=findViewById(R.id.backimage);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SupportActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.infos));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.donate));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.terms));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //initialize intent
                Intent intent = null;

                //initialize intent according to its id
                if (item.getId() == 4) {
                    intent = new Intent(SupportActivity.this, DateActivity.class);
                    //showBottomSheetDialog();
                } else if (item.getId() == 3) {
                    //intent = new Intent(SupportActivity.this, .class);
                } else if (item.getId() == 2) {
                    intent = new Intent(SupportActivity.this, AboutusActivity.class);
                } else if (item.getId() == 1) {
                    intent = new Intent(SupportActivity.this, HomeActivity.class);
                }

                //start the activity
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        //set the initial fragment to show
        bottomNavigation.show(3, true);

        //set menu item on click listener
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

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportActivity.this,DateActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        bottomNavigation.show(1, true);
        super.onBackPressed();
    }

}