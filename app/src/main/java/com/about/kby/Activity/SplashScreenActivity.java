package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.about.kby.Bsession;
import com.about.kby.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ss=findViewById(R.id.ss);
        ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bsession.getInstance().getUser_id(getApplicationContext()).equals("")) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Bsession.getInstance().getUser_id(getApplicationContext()).equals("")) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        }, 3000);
    }
}