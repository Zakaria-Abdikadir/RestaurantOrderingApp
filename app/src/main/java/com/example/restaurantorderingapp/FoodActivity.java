package com.example.restaurantorderingapp;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class FoodActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Food Menu");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        gridLayout = findViewById(R.id.gridLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        
        // Update navigation header with user email
        android.content.SharedPreferences sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        android.view.View headerView = navigationView.getHeaderView(0);
        android.widget.TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        String userEmail = sharedPrefs.getString("loggedInUser", "user@example.com");
        tvUserEmail.setText(userEmail);

        setupFoodGrid();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                finish();
                startActivity(new android.content.Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_food) {
                // Already here
                return true;
            } else if (id == R.id.nav_drinks) {
                startActivity(new android.content.Intent(this, DrinksActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_order) {
                startActivity(new android.content.Intent(this, OrderReceiptActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupFoodGrid() {
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(2);

        for (int i = 1; i <= 20; i++) {
            android.view.View cardView = getLayoutInflater().inflate(R.layout.item_food_drink, null);
            ImageView imageView = cardView.findViewById(R.id.imageView);
            TextView textView = cardView.findViewById(R.id.textView);
            TextView priceView = cardView.findViewById(R.id.priceView);

            // Load image using ImageLoader
            ImageLoader.loadFoodImage(this, imageView, i);
            
            textView.setText("Food " + i);
            priceView.setText("$" + String.format("%.2f", (10 + i * 2 + 0.99)));

            final int itemIndex = i;
            final String imageResource = "food_" + i;
            final double price = 10 + i * 2 + 0.99;

            cardView.setOnClickListener(v -> {
                CartItem item = new CartItem("Food " + itemIndex, imageResource, price, 1, "food");
                CartManager.getInstance().addItem(item);
                Toast.makeText(this, "Added to cart: Food " + itemIndex, Toast.LENGTH_SHORT).show();
                // Navigate to order receipt
                startActivity(new android.content.Intent(this, OrderReceiptActivity.class));
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels / 2 - 32;
            params.height = (int) (params.width * 1.2);
            params.setMargins(8, 8, 8, 8);
            cardView.setLayoutParams(params);

            gridLayout.addView(cardView);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            android.content.SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("loggedInUser");
            editor.apply();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Restaurant Ordering App v1.0", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

