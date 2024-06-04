package com.about.kby.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.about.kby.R;

public class ProductDetailsActivity extends AppCompatActivity {

    Button placeorder_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        placeorder_button=findViewById(R.id.placeorder_button);
        placeorder_button.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, OrderDetailActivity.class);
            startActivity(intent);
        });
    }
}