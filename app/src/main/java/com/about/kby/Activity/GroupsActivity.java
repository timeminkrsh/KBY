package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.about.kby.DateActivity;
import com.about.kby.R;

public class GroupsActivity extends AppCompatActivity {
    LinearLayout ll_personal,ll_opencircle;
    ImageView backimage,image;
    TextView txt_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ll_opencircle = findViewById(R.id.ll_opencircle);
        ll_personal = findViewById(R.id.ll_personal);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        backimage = findViewById(R.id.backimage);
        image = findViewById(R.id.image);
        ll_opencircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this,OpenGroupActivity.class);
                startActivity(intent);
            }
        });
        ll_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, PersonalExistingActivity.class);
                startActivity(intent);
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });
        txt_toolbar.setText("Groups");
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GroupsActivity.this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}