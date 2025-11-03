package com.example.restaurantorderingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private SharedPreferences sharedPreferences;
    private static final String DEFAULT_EMAIL = "admin@restaurant.com";
    private static final String DEFAULT_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check default credentials
            if (email.equals(DEFAULT_EMAIL) && password.equals(DEFAULT_PASSWORD)) {
                saveLoginState(email);
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            } else {
                // Check saved credentials
                String savedPassword = sharedPreferences.getString("password_" + email, "");
                if (password.equals(savedPassword)) {
                    saveLoginState(email);
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void saveLoginState(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loggedInUser", email);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }
}

