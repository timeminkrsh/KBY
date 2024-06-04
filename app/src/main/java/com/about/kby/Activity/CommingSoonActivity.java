package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.about.kby.Group2Activity;
import com.about.kby.R;

public class CommingSoonActivity extends AppCompatActivity {
    ImageView backimage,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comming_soon);
        backimage = findViewById(R.id.backimage);
        image = findViewById(R.id.image);
        image.setVisibility(View.GONE);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommingSoonActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}