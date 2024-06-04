package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.about.kby.R;

public class OrderConformActivity extends AppCompatActivity {
    Button bt_shopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_conform);
        bt_shopping=findViewById(R.id.bt_shopping);
        bt_shopping.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConformActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}