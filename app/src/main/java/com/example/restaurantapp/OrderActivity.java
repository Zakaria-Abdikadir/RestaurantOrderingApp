package com.example.restaurantapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.restaurantorderingapp.R;

public class OrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TextView txtOrder = findViewById(R.id.txtOrder);
        txtOrder.setText("Your order is being prepared!");
    }
}
