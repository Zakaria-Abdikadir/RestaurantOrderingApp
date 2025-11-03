package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.restaurantorderingapp.R;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Proceeding to Order...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MenuActivity.this, OrderActivity.class));
        });
    }
}
