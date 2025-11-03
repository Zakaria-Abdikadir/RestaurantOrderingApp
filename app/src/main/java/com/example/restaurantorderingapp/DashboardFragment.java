package com.example.restaurantorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    private android.view.View btnViewMenu, btnFood, btnDrinks, btnViewOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        btnViewMenu = view.findViewById(R.id.btnViewMenu);
        btnFood = view.findViewById(R.id.btnFood);
        btnDrinks = view.findViewById(R.id.btnDrinks);
        btnViewOrders = view.findViewById(R.id.btnViewOrders);

        btnViewMenu.setOnClickListener(v -> {
            // Show menu summary
            startActivity(new Intent(getActivity(), FoodActivity.class));
        });

        btnFood.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FoodActivity.class));
        });

        btnDrinks.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DrinksActivity.class));
        });

        btnViewOrders.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), OrderReceiptActivity.class));
        });

        return view;
    }
}

